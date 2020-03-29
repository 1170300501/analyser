package lexer;

import jxl.*;
import jxl.read.biff.BiffException;
import unit.Delta;
import unit.Token;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Lexer {
  private List<Delta> turns;         // 转移函数，δ(s,a)
  private Map<Integer, Token> rcvStates;  // 接收状态集
  
  public Lexer() {
    turns = new ArrayList<>();
    rcvStates = new HashMap<>();
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
    
    Map<Integer, Character> inchars = new HashMap<>();
    for (int i = 2; i < cols; i++) {
      char c = sheet.getCell(i, 0).getContents().charAt(0);
      inchars.put(i, c);
    }
    
    for (int i = 1; i < rows; i++) {
      int ord = new Integer(sheet.getCell(0, i).getContents());
      String key = sheet.getCell(1, i).getContents();
      
      if (key.length() != 0) {  // 具有token的可接收状态
        Token token = new Token(key);
        rcvStates.put(ord, token);
      }
      
      for (int j = 2; j < cols; j++) {
        char c = inchars.get(j);
        String next = sheet.getCell(j, i).getContents(); 
        
        if (next.length() != 0) {  // 存在状态转移，添加到状态转移函数集中
          Delta delta = new Delta(ord, c);
          delta.setOutstate(new Integer(next));
          turns.add(delta);
        }
      }
    }
    
    rcvStates.put(-1, new Token("ERR"));  // 添加一个可接收的ERR状态
    
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
    while (index < array.length) {
      char c = array[index];
      int copy = next;
      next = -1;
      Delta d = new Delta(copy, c);
      
      for (Delta t : turns) {  // 遍历转换函数集
        if (d.equals(t)) {
          next = t.getOutstate();  // 进入当前状态
          stack.add(next);
          break;
        }
      }
      
      if (next == -1) {  // 如果该状态不存在后继状态，进入异常处理
        stack.add(-1);
        stack = handle(stack, array);
        index = stack.size() - 1;
        next = stack.peek();
        System.out.println(begin + " "+ index + " "+next);
        word = content.substring(begin, index);
        
        if (next != 0) {  // 如果异常处理后能够找到一个最近的可接收状态
          Token token = rcvStates.get(next);
          Token newToken = new Token(token.getKey());  // 创建一个新token添加到token列表，
          newToken.setValue(word);                     // 由于hashmap是可变的
          if (!tokens.contains(newToken)) {
            tokens.add(newToken);
          } else {
            index++;
          }
          next = 0;
        }
        begin = index;
      } else {
        index++;
      }
    }
    
    // 最后一个word
    Token token = rcvStates.get(next); 
    word = content.substring(begin, index);
    System.out.println(next);
    Token newToken = new Token(token.getKey());
    newToken.setValue(word);
    tokens.add(newToken);
    
    return tokens;
  }
  
  /**
   * 异常处理
   * 
   * @param st
   * @param array
   * @return 异常处理后的栈
   */
  @SuppressWarnings("unchecked")
  public Stack<Integer> handle(Stack<Integer> st, char[] array) {
    int index = 0;
    
    Stack<Integer> stack = (Stack<Integer>) st.clone();
    
    while (!stack.isEmpty()) {  // 异常处理
      int pre = stack.peek();
      if (rcvStates.containsKey(pre) && pre != -1) {  // 退回到最近可接收状态
        break;
      } else {
        stack.pop();
      }
    }
    
    if (stack.isEmpty()) {  // 进入恐慌模式
      int len = array.length;
      boolean found = false;
      int cur = st.peek();
      index = st.size();
      
      while (index < len) {
        Delta d = new Delta(cur, array[index]);
        for (Delta t : turns) {
          if (d.equals(t)) {
            cur = t.getOutstate();  // 进入当前状态
            st.add(cur);
            found = true;
            break;
          }
        }
        if (found) {
          break;
        }
        index++;
      }
      if (!found) {
        st.add(-1);
      }
    } else {
      st = (Stack<Integer>) stack.clone();
    }
    
    return st;
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
    String s = ";++n";
    Lexer lexer = new Lexer();
    lexer.init();
    
    List<Token> tokens = lexer.discriminate(s);
    
    System.out.println(tokens.size());
    for (Token token : tokens) {
      System.out.println(token.toString());
    }
  }
}

