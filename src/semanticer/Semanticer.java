package semanticer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map.Entry;

import unit.Node;
import unit.Production;
import unit.Record;
import unit.SemError;
import unit.Token;

public class Semanticer extends SemanticRules {
  
  @Override
  public void init() { 
    lexer.init();
    keywords = lexer.getKeywords();
    
    symbols.add("S'");
    try {
        BufferedReader reader = new BufferedReader(new FileReader(new File("s.txt")));
        
        String line = reader.readLine();
        
        String[] ts = line.split(" ");
        for (int i = 0; i < ts.length; i++) {
          terminals.add(ts[i]);
        }
        
        while ((line = reader.readLine()) != null) {
          if (line.length() > 0) {
            String[] parts = line.split("\\s\\@\\s");
            String[] strs = parts[0].split("\\s->\\s");

            String[] ms = null;
            List<String[]> moves = new ArrayList<>();
            
            if (parts.length > 1) {
              ms = parts[1].split(";\\s");
              for (int j = 0; j < ms.length - 1; j++) {
                String[] elements = ms[j].split("\\s");
                moves.add(elements);
              }
            }
            
            String[] rs = strs[1].split("\\s");  // 切割得到各个文法符号

            productions.add(new Production(strs[0], rs, moves, 0));
            for (int j = 0; j < rs.length + 1; j++) {
              if (j != rs.length && rs[j].length() != 0) {
                symbols.add(rs[j]);
                if (!unTerminals.contains(rs[j]) && !terminals.contains(rs[j])) {
                  unTerminals.add(rs[j]);
                }
              }
              stateProductions.add(new Production(strs[0], rs, moves, j));  // 等于rs.length时为已规约状态
            }
            
          }
        }
        
        reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    symbols.add("$");
    terminals.add("$");
    
    setFirsts();
    setFollows();
    getCollection();
    calculate();
  }
  
  /**
   * 分析过程
   * 
   * @param content
   */
  @Override
  public boolean analyse(String content) {
    
    lexer.discriminate(content);
    List<Token> tokens = lexer.getTokens();
    
    /* 添加结束符号 */
    Token end = new Token("END");
    end.setWord("$");
    end.setLine(tokens.get(tokens.size() - 1).getLine());
    tokens.add(end);
    
    Deque<Node> convenTree = new ArrayDeque<>();
    Deque<Integer> convenOrd = new ArrayDeque<>();  // 规约序号栈
    
    convenOrd.addFirst(0);
    
    int i = 0;
    while (!convenOrd.isEmpty()) {
      Token token = tokens.get(i);
      if (token.getKey().equals("COMMENT")) {
        i++;
        continue;
      }
      String sym = dealToken(token);
      
      int now = convenOrd.peekFirst();
      int index = (terminals.contains(sym)) ? terminals.indexOf(sym) : unTerminals.indexOf(sym);
      String act = actions[now][index];
      
      if (act.equals("acc")) {
        return true;
      } else if (act.charAt(0) == 's') {  // 移入动作
        /* 入符号栈 */
        Node node = new Node(sym, token.getValue(), false);
        node.setLine(token.getLine());
        convenTree.addFirst(node);
        /* 入序号栈 */
        convenOrd.addFirst(Integer.valueOf(act.substring(1)));

        i++;
      } else if (act.charAt(0) == 'r') {  // 规约动作
        int ord = Integer.valueOf(act.substring(1));
        Production production = productions.get(ord);
        String left = production.getLeft();
        int len = production.getRights().length;

        /* 构建语法分析树 */
        boolean e = (len == 0) ? true : false;
        Node parent = new Node(left, null, e);
        Node[] nodes = new Node[len];
        
        /* 规约与语义动作 */
        convenAndMove(ord, nodes, parent, convenTree, convenOrd);
        
        int top = convenOrd.peekFirst();
        int next = gotos[top][unTerminals.indexOf(left)];  // 下一状态进栈
        convenOrd.addFirst(next);
        
      }
    }
    
    return false;
  }
  
  public String getctrCodeLines() {
    StringBuilder sb = new StringBuilder();
    
    for (Entry<Integer, String> entry : ctrCodeLines.entrySet()) {
      sb.append(entry.getKey() + ": ");
      sb.append(entry.getValue() + "\n");
    }
    
    return new String(sb);
  }
  
  public String getQuaternary() {
    StringBuilder sb = new StringBuilder();
    
    for (Entry<Integer, String> entry : ctrCodeLines.entrySet()) {
      String s = entry.getValue();
      if (s.contains("if")) {
        String[] ptrs = s.split(" ");
        sb.append("( j" + ptrs[2] + ", " + ptrs[1] + ", " + ptrs[3] + ", " + ptrs[5] + " )\n");
      } else if (s.contains("goto")) {
        String[] ptrs = s.split(" ");
        sb.append("( j, -, -, " + ptrs[1] + " )\n");
      } else if (s.contains("param")) {
        String[] ptrs = s.split(" ");
        sb.append("( param, -, -, " + ptrs[1] + " )\n");
      } else if (s.contains("call")) {
        String[] ptrs = s.split(" ");
        sb.append("( call, " + ptrs[3] + ", " + ptrs[5] + ", " + ptrs[0] + " )\n");
      } else if (s.contains("return")) {
        String[] ptrs = s.split(" ");
        sb.append("( ret, -, -, " + ptrs[1] + " )\n");
      } else if (s.contains("=")) {
        String[] ptrs = s.split(" ");
        if (!s.contains("[") && !s.contains("]") && !s.contains("= *") && s.charAt(0) != '*') {
          if (ptrs.length > 4) {
            sb.append("( " + ptrs[3] + ", " + ptrs[2] + ", " + ptrs[4] + ", " + ptrs[0] + " )\n");
          } else {
            sb.append("( =, " + ptrs[2] + ", -, " + ptrs[0] + " )\n");
          }
        } else if (s.charAt(0) == '*') {
          sb.append("( *=, " + ptrs[2] + ", -, " + ptrs[0].substring(1) + " )\n");
        } else if (s.indexOf("[") > s.indexOf("=")) {
          sb.append("( =[], " + ptrs[2] + ", " + ptrs[4] + ", " + ptrs[0] + " )\n");
        } else if (s.indexOf("[") < s.indexOf("=")) {
          sb.append("( []=, " + ptrs[5] + ", " + ptrs[0] + ", " + ptrs[2] + " )\n");
        }
      }
    }
    
    return new String(sb);
  }
  
  public String getSErrors() {
    StringBuilder sb = new StringBuilder();
    for (SemError semError : sErrors) {
      sb.append(semError + "\r\n");
    }
    return sb + "";
  }
  
  public String getTblptr() {
    StringBuilder sb = new StringBuilder();
    
    for (Record r : tblptr.keySet()) {
      sb.append("=========================================================\n");
      sb.append(r);
      sb.append("\n");
      sb.append("=========================================================\n");
      for (Record record : tblptr.get(r)) {
        sb.append(record);
        sb.append("\n");
      }
    }

    sb.append("=========================================================\n");
    
    return sb + "";
  }
  
  public static void main(String[] args) {
    Semanticer sRules = new Semanticer();
    sRules.init();
    sRules.analyse("struct a{int c, d; char f;};struct s{double c;float d; short f;};int temp(int c, int d);int main(){ int a[2], b; double s;if(a < b || b && s == 100) { a[0]= b+1; } else { do {s++;if(s > 1){s = 100;}} while(s < 200); b = temp(a[0]+b, b);} return 0; }int temp(int c, int d){char x;int r; r = r+ 1;}");
    //sRules.analyse("struct a{ int e, n; char b; };struct b{ int s; char *f; };");
    System.out.println(sRules.getSErrors());
    System.out.println(sRules.getTblptr());
  }
}
