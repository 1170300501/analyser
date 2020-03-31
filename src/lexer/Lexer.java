package lexer;

import jxl.*;
import jxl.read.biff.BiffException;
import unit.Delta;
import unit.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Lexer {
  private List<Delta> turns;              // 转移函数集，δ(s,a)
  private Map<Integer, Token> rcvStates;  // 接收状态集
  private Set<String> keywords;           // C关键字集
  
  public Lexer() {
    turns = new ArrayList<>();
    rcvStates = new HashMap<>();
    keywords = new HashSet<>();
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
    }
    
    for (int i = 1; i < rows; i++) {
      int ord = new Integer(sheet.getCell(0, i).getContents());
      String key = sheet.getCell(1, i).getContents();
      
      if (key.length() != 0) {  // 具有token的可接收状态
        Token token = new Token(key);
        rcvStates.put(ord, token);
      }
      
      for (int j = 2; j < cols; j++) {
        String s = inchars.get(j);
        String next = sheet.getCell(j, i).getContents();
        
        if (next.length() != 0) {  // 存在状态转移，添加到状态转移函数集中
          Delta delta = new Delta(ord, s);
          delta.setOutstate(new Integer(next));
          turns.add(delta);
        }
      }
    }

    book.close();
    
    rcvStates.put(-1, new Token("ERR"));  // 添加一个可接收的ERR状态
    
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
   * @return 单词对应的词法
   */
  public List<Token> discriminate(String content) {
    char[] array = content.toCharArray();
    List<Token> tokens = new ArrayList<>();  // token列表
    Stack<Integer> stack = new Stack<>();
    int next = 0;   // 初始状态
    int index = 0;  // 数组下标值
    int begin = 0;  // 一个word的初始下标
    int last = -1;   // 上一接收状态下标
    stack.add(next);
    
    String word = null;
    do {
      String in = String.valueOf(array[index]);
      int copy = next;
      next = -1;
      Delta d = new Delta(copy, in);
      
      for (Delta t : turns) {  // 遍历转换函数集
        if (d.equals(t) || d.match(t)) {
          next = t.getOutstate();  // 进入当前状态
          if (next != 0) {  // 在初始状态意味着转移字符是一个可跳过的字符
            stack.add(next);
          }
          //System.out.println(t + " "+ d + " "+d.equals(t));
          break;
        }
      }
      //System.out.println(" " + in + " " + index + " "+last);
      //System.out.println(last+" "+begin+" "+next + " "+index);
      //System.out.println(stack);
      
      if (next == -1) {  // 如果该状态不存在后继状态，进入异常处理
        int[] peer = handle(stack, array, last);
        index = peer[0];
        next = peer[1];
        last = index - 1;
        word = content.substring(begin, index);
        
        if (next != 0) {  // 如果异常处理后能够找到一个最近的可接收状态

          Token newToken;
          if (next == 1 && keywords.contains(word)) {
            newToken = new Token(word.toUpperCase());
          } else {
            Token token = rcvStates.get(next);
            newToken = new Token(token.getKey());  // 创建一个新token添加到token列表，
                                                   // 由于hashmap是可变的
          }
          
          newToken.setValue(word);                     
          tokens.add(newToken);
          
          next = 0;
          stack.clear();
          stack.add(next);
        }
        begin = index;
      } else {
        if (next == 0) {  // 遇到可跳过的字符
          last++;
          begin++;
          //System.out.println(last + " "+begin);
        }
        index++;
      }
    } while (index < array.length);
    
    // 最后一个word
    if (last != index - 1 && next != 0) {  // 如果上一接收下标为最后一个字符下标，那么不再添加token 
      if (!rcvStates.containsKey(next)) {  // 否则，当最后一个字符无法被接收，认定它是一个ERR
        next = -1;
      }
      
      word = content.substring(begin, index);
      Token newToken;
      
      if (next == 1 && keywords.contains(word)) {
        newToken = new Token(word.toUpperCase());
      } else {
        Token token = rcvStates.get(next);
        newToken = new Token(token.getKey());
      }

      newToken.setValue(word);
      tokens.add(newToken);
    }
    
    return tokens;
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
      //System.out.println("now"+now + " "+index);
      
      while (index < len) {  // 向前移动直到找到一个可接收字符
        Delta d = new Delta(now, String.valueOf(array[index]));
        for (Delta t : turns) {
          if (d.equals(t) || d.match(t)) {
            found = true;
            break;
          }
        }
        index++;
        if (found) {
          break;
        }
      }
    }
    //System.out.println(index + " "+state);
    return new int[] { index, state };
  }
  
  /**
   * 按token顺序输出所有token内容
   * 
   * @param tokens
   * @return tokens的字符串
   */
  public String tokensToString(List<Token> tokens) {
    StringBuilder builder = new StringBuilder();
    for (Token token : tokens) {
      builder.append(token.toString() + "\r\n");
    }
    
    return builder + "";
  }
  
  public static void main(String[] args) {
    String t = ",";
    String s = "int main()\r\n" + 
        "{\r\n" + 
        "    printf(\"hello\n\");\r\n" + 
        "    return 0;\r\n" + 
        "}";
    Lexer lexer = new Lexer();
    lexer.init();
    //System.out.println(s);
    List<Token> tokens = lexer.discriminate(t);
    
    for (Token token : tokens) {
      System.out.println(token.toString());
    }
  }
}

