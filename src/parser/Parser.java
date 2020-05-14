package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import lexer.Lexer;
import unit.Goto;
import unit.Node;
import unit.ParserError;
import unit.Production;
import unit.Record;
import unit.SemError;
import unit.Token;

public class Parser {
  
  protected Lexer lexer;
  protected Set<String> keywords;
  protected List<Production> productions;
  protected List<Production> stateProductions;
  protected Set<String> symbols;
  protected List<String> terminals;
  protected List<String> unTerminals;
  protected Map<String, Set<String>> firsts;
  protected Map<String, Set<String>> follows;
  protected List<Set<Production>> closures;
  protected Set<Goto> table;
  protected String[][] actions;
  protected int[][] gotos;
  protected Node root;
  protected List<ParserError> pErrors;
  
  public Parser() {
    lexer = new Lexer();
    
    productions = new ArrayList<>();
    stateProductions = new ArrayList<>();
    symbols = new TreeSet<>();
    terminals = new ArrayList<>();
    unTerminals = new ArrayList<>();
    
    firsts = new HashMap<>();
    follows = new HashMap<>();
    
    closures = new ArrayList<>();
    table = new HashSet<>();
    
    pErrors = new ArrayList<>();
  }
  
  /**
   * 初始化Paser的文法
   */
  public void init() {
    lexer.init();
    keywords = lexer.getKeywords();
    
    symbols.add("S'");
    try {
        BufferedReader reader = new BufferedReader(new FileReader(new File("g.txt")));
        
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.length() > 0) {
            String[] strs = line.split("\\s->\\s");
            String[] rights = strs[1].split("\\s\\|\\s");

            for (int i = 0; i < rights.length; i++) {
              String[] rs = rights[i].split("\\s");  // 切割得到各个文法符号
              
              productions.add(new Production(strs[0], rs, null, 0));
              for (int j = 0; j < rs.length + 1; j++) {
                if (j != rs.length && rs[j].length() != 0) {
                  symbols.add(rs[j]);
                  if ((!Character.isLetter(rs[j].charAt(0)) || rs[j].equals("IDN") || rs[j].equals("NUMBER") 
                      || rs[j].equals("CHA") || rs[j].equals("STRING") 
                      || keywords.contains(rs[j].toLowerCase())) && !terminals.contains(rs[j])) {
                    terminals.add(rs[j]);
                  } else if (!unTerminals.contains(rs[j]) && !terminals.contains(rs[j])) {
                    unTerminals.add(rs[j]);
                  }
                }
                stateProductions.add(new Production(strs[0], rs, null, j));  // 等于rs.length时为已规约状态
              }
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
   * 计算first集
   */
  public void setFirsts() {
    Set<String> calculated = new HashSet<>();   // 计算FIRST集完毕的符号集
    
    Set<String> epsilon_set = new HashSet<>();  // 空串的FIRST集
    epsilon_set.add("@");
    firsts.put("", epsilon_set);
    
    Deque<String> queue = new ArrayDeque<>();
    queue.addAll(symbols);  // 放入所有文法符号
    
    while (!queue.isEmpty()) {
      String sym = queue.pollFirst();
      Set<String> first = new HashSet<>();
      boolean toFirst = true;
      
      if (terminals.contains(sym)) {
        first.add(sym);
      } else {
        for (Production production : productions) {
          
          if (production.getLeft().equals(sym)) {  // 非终结符判断其右边的first集
            String[] rights = production.getRights();
            if (rights.length == 0) {
              first.addAll(epsilon_set);
            }
            
            for (int i = 0; i < rights.length; i++) {
              String t = rights[i];
              
              if (firsts.containsKey(t)) {
                first.addAll(firsts.get(t));
                if (toFirst) {
                  toFirst = calculated.contains(t) || t.equals(sym);  // 确认它的产生式右部不同于左部的符号的FIRST集
                                                                      // 是否已不能添加新的元素，当仍能添加时
                                                                      // 记这一文法符号的FIRST计算还不能结束
                }
                if (!firsts.get(t).contains("@")) {  // 如果FIRST集具有空串，那么将向后继续获取下一符号的FIRST集
                  break;
                }
              } else { // 存在某个当前符号的FIRST集还未计算
                toFirst = false;
                break;
              }
            }
          }
        }
        
      }
      
      firsts.put(sym, first);
      if (!toFirst) {  // 计算未结束，将其仍入队列中
        queue.addLast(sym);
      } else {  // 否则，计算已完毕，添加到完成集
        calculated.add(sym);
      }

    }
    /*for (Entry<String, Set<String>> e : firsts.entrySet()) {
      System.out.print(e.getKey() + "{");
      
      for (String string : e.getValue()) {
        System.out.print(string + " ");
      }
      
      System.out.println("}");
    }
    
    System.out.println();*/
  }
  
  /**
   * 计算follow集
   */
  public void setFollows() {
    Set<String> calculated = new HashSet<>();   // 计算FOLLOW集完毕的符号集
    
    Set<String> s_set = new HashSet<>();
    s_set.add("$");
    follows.put("S'", s_set);
    
    Deque<String> queue = new ArrayDeque<>();
    calculated.add("S'");
    queue.addAll(symbols);  // 放入所有文法符号
    
    while (!queue.isEmpty()) {
      String sym = queue.pollFirst();
      if (sym.equals("S'") || terminals.contains(sym)) {  // 终结符无需计算FOLLOW集
        continue;
      }
      
      Set<String> follow = new HashSet<>();
      boolean toFollow = true;
      
      for (Production production : productions) {
        String left = production.getLeft();
        String[] rights = production.getRights();
        
        for (int i = 0; i < rights.length; i++) {
          if (rights[i].equals(sym)) {
            
            if ((i == rights.length - 1) || (i != rights.length - 1 && firsts.get(rights[i + 1]).contains("@"))) {
              /* A -> αB 或 ( A -> αBβ 且 FIRST(β) 含有ε ) */
              boolean end = false;
              if (i != rights.length - 1) {  // 仍需添加FIRST(β)中除了ε以外的符号
                follow.addAll(firsts.get(rights[i + 1]));
                follow.remove("@");
                
                for (int j = i + 2; j < rights.length; j++) {
                  if (end) {
                    break;
                  }
                  follow.addAll(firsts.get(rights[j]));
                  
                  if (i != rights.length - 1 && !follow.remove("@")) {
                    end = true;
                  }
                }
              }
              
              if (!end && follows.containsKey(left)) {
                if (toFollow) {
                  toFollow = calculated.contains(left) || left.equals(sym);  // 同上，需考虑FOLLOW集是否仍能添加新元素
                }
                follow.addAll(follows.get(left));
              } else if (!end) {  // 如果FOLLOW(A)不存在，重新入队列等待下一次计算
                toFollow = false;
              }
            } else if (i != rights.length - 1 && !firsts.get(rights[i + 1]).contains("@")) {
              /* A -> αBβ 且 FIRST(β) 不存在 ε */
              follow.addAll(firsts.get(rights[i + 1]));
            }
          }
        }
      }

      follows.put(sym, follow);
      if (!toFollow) {  // 仍有未计算出的follow关联
        queue.addLast(sym);
      } else {
        calculated.add(sym);
      }
      
    }
    
    /*for (Entry<String, Set<String>> e : follows.entrySet()) {
      System.out.print(e.getKey() + " f{");
      
      for (String string : e.getValue()) {
        System.out.print(string + ", ");
      }
      
      System.out.println("}");
    }*/
  }
  
  /**
   * 计算CLOSURE
   * 
   * @param front
   * @param visited
   * @return
   */
  public Set<Production> getClosure(Set<Production> fronts, Set<Integer> orders) {
    Set<Production> closure = new HashSet<>();
    Set<Integer> orderColsure = new TreeSet<>();
    Deque<Production> equivalence = new ArrayDeque<>();
    
    closure.addAll(fronts);
    orderColsure.addAll(orders);
    equivalence.addAll(fronts);
    
    while (!equivalence.isEmpty()) {
      /* 获取队列头部元素 */
      Production nowPro = equivalence.pollFirst();
      int nowToConven = nowPro.getToConven();
      if (nowToConven == nowPro.getRights().length) {
        continue;
      }
      
      String nowSym = nowPro.getRights()[nowToConven];
      
      for (int i = 0; i < stateProductions.size(); i++) {
        if (!orderColsure.contains(i)) {
          Production production = stateProductions.get(i);
          if (production.getLeft().equals(nowSym) && production.getToConven() == 0) {
            /* 如果未加入任何闭包且满足闭包条件 */
            closure.add(production);
            orderColsure.add(i);
            equivalence.addLast(production);
          }
        }
      }
    }
    
    return closure;
  }
  
  public Set<Production> getGOTO(int number, String symbol) {
    Set<Production> former = closures.get(number);
    Set<Production> next = new HashSet<>();
    Set<Integer> orders = new TreeSet<>();
    
    for (Production production : former) {
      int toConven = production.getToConven();
      String[] rights = production.getRights();
      if (toConven != rights.length && rights[toConven].equals(symbol)) {
        next.add(new Production(production.getLeft(), production.getRights(), production.getMoves(), toConven + 1));
        orders.add(stateProductions.indexOf(production));
      }
    }
    
    return getClosure(next, orders);
  }
  
  /**
   * 计算LR规范集族
   */
  public void getCollection() {
    
    int next = 0;

    Set<Production> first = new HashSet<>();       // S'->·S的初始集合
    Set<Integer> orderColsure = new TreeSet<>();   // S'->·S的初始序号集合
    Set<Integer> visited = new TreeSet<>();        // 已分析过的闭包集序号集合
    Deque<Integer> keyQueue = new ArrayDeque<>();  // closure标号队列
    
    first.add(stateProductions.get(0));
    orderColsure.add(0);
    
    Set<Production> firstColsure = getClosure(first, orderColsure);  // S'->·S的闭包集合
    closures.add(firstColsure);
    next++;
    
    keyQueue.add(0);
    visited.add(0);
    
    while (!keyQueue.isEmpty()) {
      Integer now = keyQueue.pollFirst();
      
      for (String symbol : symbols) {
        Set<Production> res = getGOTO(now, symbol);
        if (!res.isEmpty()) {
          
          int toAdd = next;
          int index = closures.indexOf(res);
          
          if (index == -1) {
            keyQueue.add(next);
            closures.add(next, res);
            next++;
          } else {
            toAdd = index;
          }
          
          Goto g = new Goto(now, symbol);
          g.setLatter(toAdd);
          table.add(g);
        }
      }
    }
    
  }
  
  /**
   * 计算action和goto
   */
  public void calculate() {
    
    /* 初始化action和goto表 */
    int c_size = closures.size();
    int t_size = terminals.size();
    int u_size = unTerminals.size();
    
    actions = new String[c_size][t_size];
    gotos = new int[c_size][u_size];
    
    for (int i = 0; i < c_size; i++) {
      for (int j = 0; j < t_size; j++) {
        actions[i][j] = "error";
      }
      for (int j = 0; j < u_size; j++) {
        gotos[i][j] = -1;
      }
    }
    
    Set<Integer> visited = new TreeSet<>();
    
    for (Goto gt : table) {
      int former = gt.getFormer();
      int latter = gt.getLatter();
      String s = gt.getSymbol();
      if (terminals.contains(s)) {
        actions[former][terminals.indexOf(s)] = "s" + latter;
      } else {
        gotos[former][unTerminals.indexOf(s)] = latter;
      }
      
      Set<Production> ps = closures.get(former);
      for (Production production : ps) {
        String left = production.getLeft();
        if (production.getToConven() == production.getRights().length) {
          for (int j = 0; j < terminals.size(); j++) {
            if (follows.get(left).contains(terminals.get(j))) {  // A->α·的FOLLOW集
              actions[former][j] = "r" + productions.indexOf(new Production(left, production.getRights(), production.getMoves(), 0));
            }
          }
        }
      }
      
      visited.add(former);
    }
    
    for (int i = 0; i < closures.size(); i++) {
      if (!visited.contains(i)) {
        for (Production production : closures.get(i)) {
          String left = production.getLeft();
          if (left.equals("S'")) {
            actions[i][terminals.indexOf("$")] = "acc";
          } else {
            for (int j = 0; j < terminals.size(); j++) {
              if (follows.get(left).contains(terminals.get(j))) {  // A->α·的FOLLOW集
                actions[i][j] = "r" + productions.indexOf(new Production(left, production.getRights(), production.getMoves(), 0));
              }
            }
          }
        }
      }  
    }
    /*System.out.println();
    for (int i = 0; i < terminals.size(); i++) {
      System.out.print(terminals.get(i) + "\t");
    }
    System.out.println();
    for (int i = 0; i < actions.length; i++) {
      for (int j = 0; j < actions[0].length; j++) {
        System.out.print(actions[i][j] + "\t");
      }
      System.out.println();
    }*/
  }
  
  /**
   * 分析过程
   * 
   * @param content
   */
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
      //System.out.println("tree" + sym + " " + convenTree);
      
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
        
        for (int j = 0; j < len; j++) {  // 待规约的符号及状态序号全部出栈
          nodes[len - j - 1] = convenTree.pollFirst();
          nodes[len - j - 1].setParent(parent);
          
          convenOrd.pollFirst();
        }

        int line = (nodes.length == 0) ? 1 : nodes[0].getLine();
        parent.setChildren(nodes);
        parent.setLine(line);
        convenTree.addFirst(parent);  // 规约得到的符号进栈
        root = parent;
        
        int top = convenOrd.peekFirst();
        int next = gotos[top][unTerminals.indexOf(left)];  // 下一状态进栈
        convenOrd.addFirst(next);
        
        //doMoves(production.getMoves());
        
      } else if (act.equals("error")) {  // 出现错误条目
        Deque<Node> copySym = new ArrayDeque<>();
        Deque<Integer> copyOrd = new ArrayDeque<>();
        copySym.addAll(convenTree);
        copySym.add(new Node(sym, null, false));
        copyOrd.addAll(convenOrd);
        
        if (token.getLine() != -1) {  // 如果这已经是一个错误TOKEN，说明之前已经提示过该错误
          int line = token.getLine();
          StringBuilder sb = new StringBuilder();
          sb.append(tokens.get(i - 1).getWord());  // 添加错误开始位置的前一位符号，便于提示
          
          for (int j = i; j < tokens.size(); j++) {
            if (j == i -1 || tokens.get(j).getLine() == line) {
              sb.append(" " + tokens.get(j).getWord());
            }
          }
          ParserError err = new ParserError(line, sb + "");
          pErrors.add(err);
        }
        
        while (!copyOrd.isEmpty()) {
          copySym.pollFirst();
          int ord = copyOrd.pollFirst();
          int next = -1;
          int ter_loc = -1;
          String err_sym = null;
          boolean quit = false;
          
          for (int j = 0; j < actions[0].length; j++) {
            if (actions[ord][j].charAt(0) == 'r') {
              err_sym = terminals.get(j);
              Token te = new Token("ERR");
              te.setWord(err_sym);
              te.setLine(-2);
              tokens.add(i, te);
              quit = true;
              break;
            }
          }
          
          if (err_sym == null) {
            for (int j = 0; j < actions[0].length; j++) {
              if (actions[ord][j].charAt(0) == 's') {
                err_sym = terminals.get(j);
                Token te = new Token("ERR");
                te.setWord(err_sym);
                te.setLine(-2);
                tokens.add(i, te);
                quit = true;
                break;
              }
            }
            
            if (!quit) {
              for (int j = 0; j < gotos[0].length; j++) {
                if (gotos[ord][j] != -1) {
                  next = gotos[ord][j];
                  err_sym = unTerminals.get(j);
                  quit = true;
                  break;
                }
              }
            }
            
            /*if (next != -1) {  // 当它在goto表有转移项
              for (int j = i; j < tokens.size(); j++) {
                Token t = tokens.get(j);
                String w = dealToken(t);
                if (actions[ord][terminals.indexOf(w)].charAt(0) == 'r') {  // 当前终结符是否能使该状态规约进入下一状态
                  ter_loc = j;
                  break;
                }
              }
              
              if (ter_loc != -1) {  // 找到下一正确位置
                i = ter_loc;
                copySym.addFirst(new Node(err_sym, null, false));
                copyOrd.addFirst(next);
                break;
              }
            }*/
          }
          
          if (quit) {
            break;
          }
        }
      }
    }
    
    return false;
  }
  
  /*private void doMoves(List<String[]> moves) {
    for (String[] ms : moves) {
      //System.out.println(ms[0]);
      String v = ms[0];   // 待赋值变量
      if (v.contains("enter")) {   // enter函数，写入符号表
        String[] params = v.split("[(|)|,]");
        if (!enter(params[1], params[2], params[3])) {   // 已存在该标识符的表项
          sErrors.add(new SemError(0, "There is a variable or function repeated declaration."));
        }
      } else if (v.contains("if")) {   // 判断
        // 检查如果符合条件
      } else {   // 赋值情况
        int len = ms.length;
        String[] assign = new String[len - 2];
        
        for (int i = 2; i < len; i++) {
          assign[i - 2] = ms[i];
        }
        
        if (assign[0].contains("lookup")) {
          String[] params = v.split("[(|)]");
          Record r = lookup(params[1]);
          if (r == null) {   // 不存在该标识符的表项
            sErrors.add(new SemError(0, "The referenced variable or function has not been declared before."));
          } else {
            
          }
        } else if (assign[0].contains("newtemp")) {
          v = newtemp();
        } else if (!v.contains("\\.")) {  // 临时变量
          
        } else {   // 普通赋值
          if (assign.length == 3) {
            
          } else {
            if (assign[3].equals("+")) {
              int a1 = assign[2];
              int a2 = assign[4];
              int a3 = a1 + a2;
            } else if (assign[3].equals("*")) {
              
            }
          }
        }
      }
    }
  }*/
  
  /*private boolean enter(String id, String type, String offset) {
    // 如果已存在，返回false
    return true;
  }
  
  private Record lookup(String id) {
    // 如果不存在，返回false
    return null;
  }
  
  private String newtemp() {
    // 生成一个不在程序中的局部变量
    int loc = temps.size();
    String newtemp = "t" + loc;
    
    //temps.add(newtemp);
    return newtemp;
  }*/
  
  public String treePreToString() {
    StringBuilder sb = new StringBuilder();
    Deque<Node> stack = new ArrayDeque<>();
    Deque<Integer> layers = new ArrayDeque<>();
    
    stack.addFirst(root);
    layers.add(0);
    
    while (!stack.isEmpty()) {
      Node n = stack.pollFirst();
      Integer i = layers.pollFirst();
      if (!n.getEpsilon()) {
        for (int j = 0; j < i; j++) {
          sb.append("  ");
        }
        sb.append(n + "\r\n");
      }
      
      Node[] cs = n.getChildren();
      if (cs != null) {
        for (int j = cs.length - 1; j >= 0; j--) {
          stack.addFirst(cs[j]);
          layers.addFirst(i + 1);
        }
      }
    }
    
    return sb + "";
  }
  
  public String treeLevToString() {
    StringBuilder sb = new StringBuilder();
    Deque<Node> queue = new ArrayDeque<>();
    Deque<Integer> layers = new ArrayDeque<>();
    
    queue.addFirst(root);
    layers.add(0);
    
    int prelayer = 0;
    while (!queue.isEmpty()) {
      Node n = queue.pollFirst();
      Integer i = layers.pollFirst();
      
      Node[] cs = n.getChildren();
      if (cs != null) {
        for (int j = 0; j < cs.length; j++) {
          queue.addLast(cs[j]);
          layers.addLast(i + 1);
        }
        for (int j = 0; j < cs.length; j++) {
          sb.append("  ");
        }
      }

      sb.append(n + " ");
      if (i != prelayer) {
        sb.append("\r\n");
        prelayer = i;
      }
      System.out.println(queue);
      System.out.println(layers);
    }
    
    return sb + "";
  }
  
  public String[] getTokens() {
    return lexer.resToString();
  }
  
  public List<String> getTerminals() {
    return terminals;
  }
  
  public List<String> getUnTerminals() {
    return unTerminals;
  }
  
  public Map<String, Set<String>> getFirsts() {
    return firsts;
  }
  
  public Map<String, Set<String>> getFollows() {
    return follows;
  }
  
  public List<Set<Production>> getClosures() {
    return closures;
  }
  
  public String[][] getActions() {
    return actions;
  }
  
  public int[][] getGotos() {
    return gotos;
  }
  
  public String getPErrors() {
    StringBuilder sb = new StringBuilder();
    for (ParserError parserError : pErrors) {
      sb.append(parserError + "\r\n");
    }
    return sb + "";
  }
  
  protected String dealToken(Token token) {
    String word = token.getWord();
    String key = token.getKey();
    
    if (!token.getValue().equals("_")) {
      return key;
    } else {
      if (keywords.contains(word)) {
        return key;
      } else {
        return word; 
      }
    }
  }
  
  public static void main(String[] args) {
    Parser parser = new Parser();
    parser.init();
    //System.out.println(parser.analyse("typedef struct d{int a;int b;} D; struct d{int a;} char b(int t);long c();\n int a()\r\n{int q, w[1]; \n s = b(); for(;;) {} if(a || (b<n+1)) {w = 1;} else {w = 2;} q = 2;\n  }\n long v(){} double r(){u = u + 1;}"));
    System.out.println(parser.analyse("int i; i = i + 1;"));
    System.out.println(parser.treePreToString());
    System.out.println(parser.getPErrors());
  }
}
