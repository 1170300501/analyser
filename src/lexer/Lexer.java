package lexer;

import jxl.*;
import jxl.read.biff.BiffException;
import unit.Delta;
import unit.LexerError;
import unit.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Lexer {
  private Map<Integer, Delta> columns;       // 对应列的表达式
  private Integer[][] turns;                 // 转移函数列表，δ(s,a)
  private Map<Integer, Token> rcvStates;     // 接收状态集
  private Set<String> keywords;              // C关键字集
  private List<Token> tokens;                // token列表
  private List<LexerError> errs;             // 错误列表
  
  
  public Lexer() {
    columns = new HashMap<>();
    rcvStates = new HashMap<>();
    keywords = new HashSet<>();
    tokens = new ArrayList<>();
    errs = new ArrayList<>();
  }
  
  /**
   * 读入DFA转换表
   */
  public void init() {
    Workbook book = null;
    try {
      book = Workbook.getWorkbook(new File("table.xls"));
    } catch (BiffException | IOException e) {
      e.printStackTrace();
      System.exit(0);
    }
    
    // 获取sheet
    Sheet sheet = book.getSheet(0);
    
    int cols = sheet.getColumns(); // 列
    int rows = sheet.getRows();    // 行
    
    Map<Integer, String> inchars = new HashMap<>();  // 存储转移符号
    for (int i = 2; i < cols; i++) {
      String s = sheet.getCell(i, 0).getContents();
      inchars.put(i, s);
      columns.put(i, new Delta(s));
    }
    
    turns = new Integer[rows][cols];
    
    for (int i = 1; i < rows; i++) {
      int ord = new Integer(sheet.getCell(0, i).getContents());
      String key = sheet.getCell(1, i).getContents();
      
      if (key.length() != 0) {  // 具有token的可接收状态
        Token token = new Token(key);
        rcvStates.put(ord, token);
      }
      
      for (int j = 2; j < cols; j++) {
        String next = sheet.getCell(j, i).getContents();
        
        if (next.length() != 0) {  // 存在状态转移，添加到状态转移函数集中
          turns[ord][j] = Integer.valueOf(next);
        }
      }
    }

    book.close();
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader(new File("keywords.txt")));
      
      String line;
      while ((line = reader.readLine()) != null) {
        keywords.add(line);
      }
      
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * DFA识别词法过程
   * 
   * @param string  输入单词
   */
  public void discriminate(String content) {
    char[] array = content.toCharArray();
    Stack<Integer> stack = new Stack<>();
    int next = 0;   // 初始状态
    int index = 0;  // 数组下标值
    int last = -1;  // 上一接收状态下标
    
    stack.add(next);
    
    String word = null;
    do {
      String in = String.valueOf(array[index]);
      int now = next;
      next = -1;
      
      if (now == 0 && Character.isWhitespace(array[index])) {  // 在初始状态意味着转移字符是一个可跳过的字符
        next = 0;
      } else {
        Delta d = new Delta(in);
        for (int i = 0; i < turns[0].length; i++) {
          Delta t = columns.get(i);
          if (turns[now][i] != null && (d.equals(t) || d.match(t))) {
            next = turns[now][i];
            stack.add(next);
            break;
          }
        }
      }
      
      if (next == -1) {  // 如果该状态不存在后继状态，进入异常处理
        int[] peer = handle(stack, array, last);
        index = peer[0];
        next = peer[1];
        
        word = content.substring(last + 1, index);
        int line = getlines(content, last, index, word);
        
        addWord(line, next, word);
        
        last = index - 1;
        next = 0;
        stack.clear();
        stack.add(next);
      } else {
        if (next == 0) {  // 遇到可跳过的字符
          last++;
        }
        index++;
      }
    } while (index < array.length);
    
    // 最后一个word
    if (last != index - 1 && next != 0) {  // 如果上一接收下标为最后一个字符下标，那么不再添加token 
      if (!rcvStates.containsKey(next)) {  // 否则，当最后一个字符无法被接收，认定它是一个ERR
        next = -1;
      }
      word = content.substring(last + 1, index);
      int line = getlines(content, last, index, word);
      
      addWord(line, next, word);
    }
    
  }
  
  /**
   * 异常处理
   * 
   * @param st
   * @param array
   * @return 上一接收下标
   */
  @SuppressWarnings("unchecked")
  public int[] handle(Stack<Integer> st, char[] array, int last) {
    int index = 0;
    int state = -1;
    Stack<Integer> stack = (Stack<Integer>) st.clone();
    
    while (!stack.isEmpty()) {  // 异常处理
      int pre = stack.peek();
      if (rcvStates.containsKey(pre)) {  // 退回到最近可接收状态
        index = last + stack.size();  // 下一开始输入字符下标
        state = pre;  // 上一接收状态
        break;
      } else {
        stack.pop();
      }
    }
    
    if (stack.isEmpty()) {  // 进入恐慌模式
      int len = array.length;
      boolean found = false;
      
      index = last + st.size();  // 从当前字符的下一字符开始寻找
      int now = st.peek();
      
      while (index < len) {  // 向前移动直到找到一个可接收字符
        Delta d = new Delta(String.valueOf(array[index]));
        boolean space = false;
        if (Character.isWhitespace(array[index])) {
          space = true;
        }
        for (int i = 0; i < turns[0].length; i++) {
          if (rcvStates.containsKey(now) || space) { // 当遇到空格时同样视为该单词结束
            found = true;
            break;
          } else {
            Delta t = columns.get(i);
            if (turns[now][i] != null && (d.equals(t) || d.match(t))) {
              now = i;
            }
          }
        }
        if (!space) {
          index++;
        }
        if (found) {
          break;
        }
      }
    }
    return new int[] { index, state };
  }
  
  public Set<String> getKeywords() {
    return keywords;
  }

  /**
   * 按token顺序输出所有token内容
   * 
   * @param tokens
   * @return tokens的字符串
   */
  public String[] resToString() {
    String[] res = new String[2];
    
    StringBuilder builder1 = new StringBuilder();
    for (Token token : tokens) {
      builder1.append(token.toString() + "\r\n");
    }
    
    StringBuilder builder2 = new StringBuilder();
    for (LexerError err : errs) {
      builder2.append(err.toString() + "\r\n");
    }
    
    res[0] = builder1 + "";
    res[1] = builder2 + "";
    
    return res;
  }
  
  public List<Token> getTokens() {
    return tokens;
  }
  
  /**
   * 添加一个token或error
   * 
   * @param nowline
   * @param next
   * @param word
   */
  private void addWord(int line, int next, String word) {
    if (next == -1) {  // 识别到一个错误
      errs.add(new LexerError(line, word));
    } else {
      Token newToken;
      
      if (next == 1 && keywords.contains(word)) {
        newToken = new Token(word.toUpperCase());
      } else {
        Token token = rcvStates.get(next);
        newToken = new Token(token.getKey());  // 创建一个新token添加到token列表，
                                               // 由于hashmap是可变的
      }
      
      newToken.setValue(word);    
      newToken.setWord(word);
      newToken.setLine(line);
      tokens.add(newToken);

    }
    
  }
  
  /**
   * 获取行数
   * 
   * @param array
   * @param index
   * @return
   */
  private int getlines(String content, int last, int index, String word) {
    /*int line = 1;
    int pre = 0;
    if (tokens.size() != 0) {
      Token t = tokens.get(tokens.size() - 1);
      pre = last;
      line = t.getLine();
      for (int j = pre; j < index - word.length(); j++) {
        if (content.charAt(j) == '\n') {
          line++;
        }
      }
      System.out.println(tokens + word + " " +last + " " + pre + " " + (index - word.length()));
    }*/
    int line = 1;
    for (int i = 0; i < last + 1; i++) {
      if (content.charAt(i) == '\n') {
        line++;
      }
    }
    
    return line;
  }
  
  public static void main(String[] args) {
    Lexer lexer = new Lexer();
    lexer.init();
    lexer.discriminate("    char name;\r\n" + 
        "    unsigned int id;\r\n" + 
        "    double account;\r\n" + 
        "} St;\r\n" + 
        "\r\n" + 
        "char *num();\r\n" + 
        "double func(int i, long j);");
    System.out.println("!"+lexer.resToString()[0]);
    System.out.println("#"+lexer.resToString()[1]);
  }
}

