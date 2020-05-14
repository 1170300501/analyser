package semanticer;

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
import java.util.TreeMap;

import parser.Parser;
import unit.CtrlSet;
import unit.Node;
import unit.Production;
import unit.Record;
import unit.SemError;
import unit.Token;

public class SemanticRules extends Parser {

  private Map<String, String> temps;
  private Set<CtrlSet> cSet;
  protected Map<Integer, String> ctrCodeLines;
  protected Map<Record, List<Record>> tblptr;
  protected List<SemError> sErrors;
  private int numOfT;
  private int numOfL;
  
  public SemanticRules() {
    super();
    temps = new HashMap<>();
    cSet = new HashSet<>();
    ctrCodeLines = new TreeMap<>();
    tblptr = new HashMap<>();
    sErrors = new ArrayList<>();
    numOfT = 1;
    numOfL = 1;
  }
  
  public void convenAndMove(int ord, Node[] nodes, Node parent, Deque<Node> convenTree, Deque<Integer> convenOrd) {
    int len = nodes.length;
    
    for (int j = 0; j < len; j++) {  // 待规约的符号及状态序号全部出栈
      nodes[len - j - 1] = convenTree.pollFirst();
      nodes[len - j - 1].setParent(parent);
      
      convenOrd.pollFirst();
    }
    
    //System.out.println(convenTree);
    //System.out.println(temps);
    //System.out.println(cSet);
    //System.out.println(tblptr.get(new Record("main", "Rint")));
    //System.out.println(ord + " "+productions.get(ord));
    //System.out.println(ctrCodeLines);
    
    Node t, s;
    
    switch (ord) {
    case 4:
    case 5:
    case 6:
    case 104:
    case 105:
    case 106:
    case 111:
    case 112:
    case 113:
      semanticRule4(parent);
      break;
    case 7:
    case 8:
      semanticRule7(nodes, parent);
      break;
    case 14:
      semanticRule14(nodes, parent);
      break;
    case 15:
        semanticRule15(nodes, parent);
        break;
    case 16:
      semanticRule16(nodes, parent);
      break;
    case 17:
      semanticRule17(nodes, parent);
      break;
    case 18:
      semanticRule18(nodes, parent);
      break;
    case 19:
      semanticRule19(nodes, parent);
      break;
    case 20:
      semanticRule20(nodes, parent);
      break;
    case 21:
      semanticRule21(nodes, parent);
      break;
    case 22:
      semanticRule22(nodes, parent);
      break;
    case 23:
    case 24:
    case 25:
    case 26:
    case 27:
    case 28:
    case 29:
      semanticRule23(nodes, parent);
      break;
    case 31:
      t = convenTree.peekFirst();   // 获取顶部元素
      semanticRule31(parent, t);
      break;
    case 40:
      semanticRule40(nodes, parent);
      break;
    case 41:
      semanticRule41();
      break;
    case 46:
      t = convenTree.peekFirst();   // 获取顶部元素
      semanticRule46(parent, t);
      break;
    case 49:
      semanticRule49(nodes, parent);
      break;
    case 59:
      semanticRule59(nodes, parent);
      break;
    case 51:
    case 52:
    case 56:
    case 57:
    case 58:
      semanticRule51(nodes, parent);
      break;
    case 53:
    case 54:
        semanticRule53(nodes, parent);
        break;
    case 50:
    case 55:
    case 60:
        semanticRule55(nodes, parent);
        break;
    case 61:
      semanticRule61(nodes, parent);
      break;
    case 62:
      semanticRule62(nodes, parent);
      break;
    case 63:
      semanticRule63(nodes, parent);
      break;
    case 64:
      semanticRule64(nodes, parent);
      break;
    case 65:
      semanticRule65(nodes, parent);
      break;
    case 66:
      semanticRule66(nodes, parent);
      break;
    case 67:
      semanticRule67(nodes, parent);
      break;
    case 68:
      semanticRule68(nodes, parent);
      break;
    case 69:
    case 70:
      semanticRule69(nodes, parent);
      break;
    case 71:
      semanticRule71(nodes, parent);
      break;
    case 72:
      semanticRule72(nodes, parent);
      break;
    case 73:
      t = convenTree.pollFirst();   // 获取顶部元素
      s = convenTree.peekFirst();   // 获取第二个顶部元素
      convenTree.addFirst(t);
      semanticRule73(parent, t, s);
      break;
    case 76:
      semanticRule76(nodes, parent);
      break;
    case 77:
      semanticRule77(nodes, parent);
      break;
    case 80:
      semanticRule80(nodes, parent);
      break;
    case 81:
      semanticRule81(nodes, parent);
      break;
    case 82:
    case 84:
      semanticRule82(parent);
      break;
    case 83:
      semanticRule83(nodes, parent);
      break;
    case 85:
    case 86:
    case 87:
    case 88:
    case 89:
    case 90:
      semanticRule85(nodes, parent);
      break;
    case 91:
      semanticRule91(nodes, parent);
      break;
    case 92:
      semanticRule92(nodes, parent);
      break;
    case 93:
      semanticRule93(nodes, parent);
      break;
    case 94:
      semanticRule94(parent);
      break;
    case 95:
    case 96:
      semanticRule95(nodes, parent);
      break;
    case 97:
    case 98:
      semanticRule97(nodes, parent);
      break;
    case 99:
      semanticRule99(parent);
      break;
    case 100:
      semanticRule100(parent);
      break;
    case 101:
      semanticRule101(nodes, parent);
      break;
    case 102:
      semanticRule102(nodes, parent);
      break;
    case 103:
      semanticRule103(nodes, parent);
      break;
    case 109:
      semanticRule109(nodes, parent);
      break;
    case 110:
      semanticRule110(parent);
      break;
    case 114:
      semanticRule114(parent);
      break;
      
    default:
      break;
    }
    
    int line = (nodes.length == 0) ? 1 : nodes[0].getLine();
    parent.setChildren(nodes);
    parent.setLine(line);
    convenTree.addFirst(parent);  // 规约得到的符号进栈
    root = parent;
  }
  

  /**
   * CONTS -> CONTS BLOCK
   * CONTS -> CONTS SENT
   * CONTS ->  
   * 
   * C -> ;
   * C -> SENT
   * C -> { CONTS }
   * E3 -> EVN
   * E3 -> E'
   * E3 -> 
   * 
   * CONTS.nextlist = makelist(nextline);
   * C.nextlist = makelist(nextline);
   * E3.nextlist = makelist(nextline);
   * 
   * @param parent
   */
  private void semanticRule4(Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    parent.addAttr("nextlist", makelist(nextline));
  }
  
  /**
   * BLOCK -> BRCH
   * BLOCK -> CIR
   * 
   * backpatch(BRCH.nextlist, nextline);
   * backpatch(CIR.nextlist, nextline);
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule7(Node[] nodes, Node parent) {

    backpatch(nodes[0].getAttr("nextlist"), temps.get("nextline"));
  }
  
  /**
   * REL -> REL || M2 R
   * REL.truelist = merge( REL1.truelist, R.truelist ); REL.falselist = R.falselist; backpatch(REL1.falselist, M.line );
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule14(Node[] nodes, Node parent) {
    
    parent.addAttr("truelist", merge(nodes[0].getAttr("truelist"), nodes[3].getAttr("truelist")));
    parent.addAttr("falselist", nodes[3].getAttr("falselist"));
    
    backpatch(nodes[0].getAttr("falselist"), nodes[2].getAttr("line"));
  }
  
  /**
   * REL -> REL && M2 R
   * REL.falselist = merge( REL1.falselist, R.falselist ); REL.truelist = R.truelist; 
   * backpatch(REL1.truelist, M2.line );
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule15(Node[] nodes, Node parent) {
    
    parent.addAttr("falselist", merge(nodes[0].getAttr("falselist"), nodes[3].getAttr("falselist")));
    parent.addAttr("truelist", nodes[3].getAttr("truelist"));
    
    backpatch(nodes[0].getAttr("truelist"), nodes[2].getAttr("line"));
  }
  
  /**
   * REL -> ! R
   * REL.truelist = R.falselist;
   * REL.falselist = R.truelist;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule16(Node[] nodes, Node parent) {
    
    parent.addAttr("truelist", nodes[1].getAttr("falselist"));
    parent.addAttr("falselist", nodes[1].getAttr("truelist"));
    
  }
  
  /**
   * REL -> R
   * REL.truelist = R.truelist;
   * REL.falselist = R.falselist;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule17(Node[] nodes, Node parent) {
    
    parent.addAttr("truelist", nodes[0].getAttr("truelist"));
    parent.addAttr("falselist", nodes[0].getAttr("falselist"));
    
  }
  
  /**
   * ID -> IDN
   * ID.lexeme = IDN;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule18(Node[] nodes, Node parent) {
    parent.addAttr("lexeme", nodes[0].getValue());
  }
  
  /**
   * ID -> * IDN
   * ID.lexeme = IDN; t = pointer(t); w = 4;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule19(Node[] nodes, Node parent) {
    parent.addAttr("lexeme", nodes[1].getValue());
    String t = temps.get("t");
    String w = "4";
    t += "*";
    
    temps.put("t", t);
    temps.put("w", w);
  }
  
  /**
   * V -> ID V'
   * enter(label, ID.lexeme, V'.type, V'.width);
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule20(Node[] nodes, Node parent) {
    String label = temps.get("label");
    
    if (!enter(label, nodes[0].getAttr("lexeme"), nodes[1].getAttr("type"), Integer.valueOf(nodes[1].getAttr("width")))) {
      sErrors.add(new SemError(nodes[0].getLine(), "There is a variable or function repeated declaration."));
    }
  }
  
  /**
   * V' -> [ E' ] V'
   * V'.type = array(E'.addr,V'1.type); V'.width = E'.addr * V'1.width;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule21(Node[] nodes, Node parent) {
    String val = nodes[1].getAttr("addr");
    String type = nodes[3].getAttr("type");
    String width = nodes[3].getAttr("width");
    int pwidth = Integer.valueOf(val) * Integer.valueOf(width);
    
    parent.addAttr("type", array(val, type));
    parent.addAttr("width", String.valueOf(pwidth));
  }
  
  /**
   * V' ->  
   * V'.type = t; V'.width = w;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule22(Node[] nodes, Node parent) { 
    parent.addAttr("type", temps.get("t"));
    parent.addAttr("width", temps.get("w"));
  }
  
  /**
   * TYPE -> INT | LONG | SHORT | FLOAT | DOUBLE | CHAR | VOID
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule23(Node[] nodes, Node parent) {
    parent.addAttr("type", nodes[0].getSymbol().toLowerCase());
    
    int width = 0;
    switch (nodes[0].getSymbol()) {
    case "INT":
      width = 4;
      break;
    case "LONG":
      width = 8;
      break;
    case "SHORT":
      width = 2;
      break;
    case "FLOAT":
      width = 4;
      break;
    case "DOUBLE":
      width = 8;
      break;
    case "CHAR":
      width = 1;
      break;

    default:
      break;
    }
    
    parent.addAttr("width", String.valueOf(width));
  }
  
  /**
   * VAR -> TYPE M3 T' ;
   * VAR.width = offset - s;
   * 
   * @param nodes
   * @param parent
   */
  /*private void semanticRule30(Node[] nodes, Node parent) {
    
    int s = Integer.valueOf(temps.get("s"));
    int offset = Integer.valueOf(temps.get("offset"));
    
    parent.addAttr("width", String.valueOf(offset - s));
  }*/
  
  /**
   * M3 ->  
   * t = TYPE.type; w = TYPE.width;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule31(Node parent, Node top) {
    
    temps.put("t", top.getAttr("type"));
    temps.put("w", top.getAttr("width"));
    //temps.put("s", temps.get("offset"));
  }
  
  /**
   * CALL -> ID ( U ) ;
   * p = lookupLabel(ID.lexeme); if p == null then error;
   * CALL.type = p.type;
   * CALL.addr = newtemp(); gen( CALL.addr '= call' ID.lexeme ',' U.num );
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule40(Node[] nodes, Node parent) {
    
    Record r = lookupRecord(nodes[0].getAttr("lexeme"));
    
    if (r == null) {
      sErrors.add(new SemError(nodes[0].getLine(), "The referenced variable or function has not been declared before."));
      
      parent.addAttr("type", "error");
      parent.addAttr("addr", "error");
      
    } else {
      
      parent.addAttr("type", r.getType().substring(1));
      
      parent.addAttr("addr", newtemp());
      
      String[] params = new String[] { parent.getAttr("addr"), "= call", nodes[0].getAttr("lexeme"), ",", nodes[2].getAttr("num") };
      
      int nextline = Integer.valueOf(temps.get("nextline"));
      
      ctrCodeLines.put(nextline, gen(params));
      
      temps.put("nextline", String.valueOf(nextline + 1));
      
    }
    
  }
  
  /**
   * M1 ->  
   * nextline = 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule41() {
    //temps.put("offset", "0");
    temps.put("nextline", "1");
  }
  
  /**
   * RCD -> STRUCT IDN M5 { VB } ;
   * changeWidth(IDN, VB.width);
   * 
   * @param nodes
   * @param parent
   */
  /*private void semanticRule42(Node[] nodes, Node parent) {
    int w = Integer.valueOf(nodes[4].getAttr("width"));
    changeWidth(nodes[1].getValue(), w);
  }*/
  
  /**
   * VB -> VB VAR
   * VB.width = VB1.width + VAR.width;
   * 
   * @param nodes
   * @param parent
   */
  /*private void semanticRule44(Node[] nodes, Node parent) { 
    
    int width = Integer.valueOf(nodes[0].getAttr("width")) + Integer.valueOf(nodes[1].getAttr("width"));
    
    parent.addAttr("width", String.valueOf(width));
  }*/
  
  /**
   * VB ->  
   * VB.width = 0;
   * 
   * @param nodes
   * @param parent
   */
  /*private void semanticRule45(Node parent) {
    parent.addAttr("width", "0");
  }*/
  
  /**
   * M5 ->  
   * enterlabel(IDN, 'struct');
   * label = IDN;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule46(Node parent, Node top) {
    if (!enterLabel(top.getValue(), "struct")) {  // 添加结构体
      sErrors.add(new SemError(top.getLine(), "There is a variable or function repeated declaration."));
    } else {
      temps.put("label", top.getValue());
    }
  }
  
  /**
   * E -> RETURN E'
   * 
   * gen( 'return ' E'.addr ); nextline += 1;
   * 
   * @param parent
   */
  private void semanticRule49(Node[] nodes, Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    String[] params = new String[] { "return", nodes[1].getAttr("addr") };
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
  }
  
  /**
   * T -> - F
   * T.addr = newtemp(); gen( T.addr '=' 'minus' F.addr );
   * T.type = F.type;
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule59(Node[] nodes, Node parent) {
    
    parent.addAttr("addr", newtemp());
    
    parent.addAttr("type", nodes[1].getAttr("type"));
    
    String[] params = new String[] { parent.getAttr("addr"), "= minus", nodes[1].getAttr("addr") };
    
    int nextline = Integer.valueOf(temps.get("nextline"));
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
  }
  
  /**
   * E' -> E' - T | E' + T
   * T -> T * F | T / F | T % F
   * 
   * if !match(E'1.type, T.type) then error;
   * E'.type = E'1.type;
   * 
   * if !match(T1.type, F.type) then error;
   * T.type = T1.type;
   * 
   * E'.addr = newtemp(); gen( E'.addr '=' E'1.addr op T.addr );
   * T.addr = newtemp(); gen( T.addr '=' T1.addr op F.addr );
   * 
   * nextline += 1;
   * 
   * @param parent
   * @param top
   */
  private void semanticRule51(Node[] nodes, Node parent) {
    
    if (!match(nodes[0].getAttr("type"), nodes[2].getAttr("type"), false)) {
      
      sErrors.add(new SemError(nodes[0].getLine(), "The types of operation components in '" + nodes[1].getSymbol() + "' are inconsistent."));
      
    } else {
      parent.addAttr("addr", newtemp());
      parent.addAttr("type", nodes[0].getAttr("type"));
      
      String[] params = new String[] { parent.getAttr("addr"), "=", nodes[0].getAttr("addr"), nodes[1].getSymbol(), nodes[2].getAttr("addr") };
  
      int nextline = Integer.valueOf(temps.get("nextline"));
      
      ctrCodeLines.put(nextline, gen(params));
      
      temps.put("nextline", String.valueOf(nextline + 1));
    }
  }
  
  /**
   * E' -> E' -- | E' ++
   * E'.addr = newtemp(); gen(E'1.addr sop );
   * E'.type = E'1.type;
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule53(Node[] nodes, Node parent) {
    
    parent.addAttr("addr", newtemp());
    parent.addAttr("type", nodes[0].getAttr("type"));
    
    String[] params = new String[] { nodes[0].getAttr("addr"), nodes[1].getSymbol() };

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
  }
  
  /**
   * E' -> T
   * T -> F
   * E'.addr = T.addr; E'.type = T.type;
   * T.addr = F.addr; T.type = F.type;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule55(Node[] nodes, Node parent) {
    parent.addAttr("addr", nodes[0].getAttr("addr"));
    parent.addAttr("type", nodes[0].getAttr("type"));
  }
  
  /**
   * F -> ( E' )
   * F.addr = E'.addr ; F.type = E'.type;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule61(Node[] nodes, Node parent) {
    parent.addAttr("addr", nodes[1].getAttr("addr"));
    parent.addAttr("type", nodes[1].getAttr("type"));
  }
  
  /**
   * F -> NUMBER
   * F.addr = NUMBER; F.type = NUMBER.type;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule62(Node[] nodes, Node parent) {
    parent.addAttr("addr", nodes[0].getValue());
    
    String type = (nodes[0].getValue().contains(".")) ? "double" : "int";
    parent.addAttr("type", type);
  }
  
  /**
   * F -> ID
   * record = lookup(ID.lexeme); if record == null then error; F.addr = record.id; F.type = record.type;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule63(Node[] nodes, Node parent) {
    Record record = lookup(temps.get("label"), nodes[0].getAttr("lexeme"));
    
    if (record == null) {
      sErrors.add(new SemError(nodes[0].getLine(), "The referenced variable or function has not been declared before."));
      parent.addAttr("addr", "error");
      parent.addAttr("type", "error");
    } else {
      parent.addAttr("addr", record.getId());
      parent.addAttr("type", record.getType());
    }
  }
  
  /**
   * F -> X
   * F.addr = newtemp(); F.type = X.type; 
   * gen( F.addr '=' X.array '[' X.offset ']' ); nextline += 1;
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule64(Node[] nodes, Node parent) {
    
    parent.addAttr("type", nodes[0].getAttr("type"));
    parent.addAttr("addr", newtemp());
    
    String[] params = new String[] { parent.getAttr("addr"), "=", nodes[0].getAttr("array"), "[", nodes[0].getAttr("offset"), "]" };

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
  }
  
  /**
   * X -> ID [ E' ]
   * record = lookup(ID.lexeme); if record == null then error; X.array = record.id;
   * X.type = X.array.type.elem; X.offset = newtemp();
   * X.nextwidth = record.width / X.type.size; 
   * gen( X.offset '=' E'.addr '*' X.type.width );
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule65(Node[] nodes, Node parent) {
    
    Record r = lookup(temps.get("label"), nodes[0].getAttr("lexeme"));
    if (r == null) {
      sErrors.add(new SemError(nodes[0].getLine(), "The referenced array has not been declared before."));
      parent.addAttr("array", "error");
    } else {
      parent.addAttr("array", r.getId());
      
      if (!nodes[2].getAttr("type").equals("int")) {

        sErrors.add(new SemError(nodes[0].getLine(), "The array subscript is not an integer."));
        
        parent.addAttr("type", "error");
        parent.addAttr("nextwidth", "error");
        parent.addAttr("offset", "error");
        
      } else {
        String type = r.getType();
        int second = type.indexOf(",");
        
        if (!type.contains("array")) {
          
          sErrors.add(new SemError(nodes[0].getLine(), "This variable isn't an array."));
          
          parent.addAttr("type", "error");
          parent.addAttr("nextwidth", "error");
          parent.addAttr("offset", "error");
          
        } else {
  
          String elem = type.substring(second + 2, type.length() - 1);   // 里层的type
          
          int size = Integer.valueOf(type.substring(6, second));   // 外层大小
          int width = Integer.valueOf(r.getWidth()) / size;
          
          parent.addAttr("type", elem);
          parent.addAttr("nextwidth", String.valueOf(width));
          parent.addAttr("offset", newtemp());
          
          String[] params = new String[] { parent.getAttr("offset"), "=", nodes[2].getAttr("addr"), "*", String.valueOf(width) };
  
          int nextline = Integer.valueOf(temps.get("nextline"));
          
          ctrCodeLines.put(nextline, gen(params));
          
          temps.put("nextline", String.valueOf(nextline + 1));
        }
      }
      
    }
  }
  
  /**
   * X -> X [ E' ]
   * X.array = X1.array; X.type = X1.type.elem; 
   * x = newtemp(); gen( x '=' N.addr '*' X.type.width ); 
   * X.nextwidth = X1.nextwidth / X1.type.size; X.offset = newtemp(); 
   * gen( X.offset '=' X1.offset '+' x ); nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule66(Node[] nodes, Node parent) {
    parent.addAttr("array", nodes[0].getAttr("array"));
    
    if (!nodes[2].getAttr("type").equals("int")) {

      sErrors.add(new SemError(nodes[0].getLine(), "The array subscript is not an integer."));
      
      parent.addAttr("type", "error");
      parent.addAttr("nextwidth", "error");
      parent.addAttr("offset", "error");
      
    } else {
      String type = nodes[0].getAttr("type");
      if (!type.contains(",")) {
        
        sErrors.add(new SemError(nodes[0].getLine(), "This array access operator is out of bounds."));
        
        parent.addAttr("type", "error");
        parent.addAttr("nextwidth", "error");
        parent.addAttr("offset", "error");
        
      } else {

        int second = type.indexOf(",");
        
        String elem = type.substring(second + 2, type.length() - 1);   // 里层的type
        
        int size = Integer.valueOf(type.substring(6, second));   // 外层大小
        int width = Integer.valueOf(nodes[0].getAttr("nextwidth")) / size;

        parent.addAttr("type", elem);
        parent.addAttr("nextwidth", String.valueOf(width));
        
        String x = newtemp();
        parent.addAttr("offset", newtemp());
        
        String[] params1 = new String[] { x, "=", nodes[2].getAttr("addr"), "*", String.valueOf(width) };
        String[] params2 = new String[] { parent.getAttr("offset"), "=", nodes[0].getAttr("offset"), "+", x };

        int nextline = Integer.valueOf(temps.get("nextline"));
        
        ctrCodeLines.put(nextline, gen(params1));
        ctrCodeLines.put(nextline + 1, gen(params2));
        
        temps.put("nextline", String.valueOf(nextline + 2));
      }
    }
  }
  
  /**
   * EVN -> X = RIGHT
   * if !match(X.type, RIGHT.type) then error;
   * gen( X.array '[' X.offset ']' '=' RIGHT.addr );
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule67(Node[] nodes, Node parent) {
    
    if (!match(nodes[0].getAttr("type"), nodes[2].getAttr("type"), true)) {
      
      sErrors.add(new SemError(nodes[0].getLine(), "The type of assigned variable is different from the type of assigning."));
      
    } else {

      String[] params = new String[] { nodes[0].getAttr("array"), "[", nodes[0].getAttr("offset"), "] =", nodes[2].getAttr("addr") };
      
      int nextline = Integer.valueOf(temps.get("nextline"));
      
      ctrCodeLines.put(nextline, gen(params));
      
      temps.put("nextline", String.valueOf(nextline + 1));
    }
    
  }
  
  /**
   * EVN -> ID = RIGHT
   * p = lookup(X.lexeme); if p == null then error;
   * if !match(ID.type, RIGHT.type) then error;
   * gen( p '=' RIGHT.addr ); nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule68(Node[] nodes, Node parent) {
    
    Record r = lookup(temps.get("label"), nodes[0].getAttr("lexeme"));
    
    if (r == null) {
      
      sErrors.add(new SemError(nodes[0].getLine(), "The assigned variable has not been declared before."));
      
    } else {
      
      if (!match(r.getType(), nodes[2].getAttr("type"), true)) {
        
        sErrors.add(new SemError(nodes[0].getLine(), "The type of assigned variable is different from the type of assigning."));
        
      } else {

        String[] params = new String[] { r.getId(), "=", nodes[2].getAttr("addr") };

        int nextline = Integer.valueOf(temps.get("nextline"));
        
        ctrCodeLines.put(nextline, gen(params));
        
        temps.put("nextline", String.valueOf(nextline + 1));
      }
      
    }
    
  }
  
  /**
   * RIGHT -> E'
   * RIGHT -> CALL
   * 
   * RIGHT.addr = E'.addr;
   * RIGHT.type = E'.type;
   * 
   * RIGHT.addr = CALL.addr;
   * RIGHT.type = CALL.type;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule69(Node[] nodes, Node parent) {
    
    parent.addAttr("addr", nodes[0].getAttr("addr"));
    parent.addAttr("type", nodes[0].getAttr("type"));

  }
  
  /**
   * RIGHT -> CHA
   * 
   * RIGHT.addr = CHA;
   * RIGHT.type = char;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule71(Node[] nodes, Node parent) {
    
    parent.addAttr("addr", nodes[0].getValue());
    parent.addAttr("type", "char");

  }
  
  /**
   * RIGHT -> STRING
   * 
   * RIGHT.addr = STRING;
   * RIGHT.type = char*;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule72(Node[] nodes, Node parent) {
    
    parent.addAttr("addr", nodes[0].getValue());
    parent.addAttr("type", "char*");

  }
  
  /**
   * M4 ->  
   * enterLabel(IDN, 'R' + P'.type);
   * label = IDN;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule73(Node parent, Node top, Node second) {
    
    enterLabel(top.getValue(), "R" + second.getAttr("type"));
    
    temps.put("label", top.getValue());
  }
  
  /**
   * P' -> TYPE *
   * P'.type = pointer(TYPE.type); P'.width = 4;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule76(Node[] nodes, Node parent) {
    String type = nodes[0].getAttr("type") + "*";
    
    parent.addAttr("type", type);
    parent.addAttr("width", String.valueOf(4));
  }
  
  /**
   * P' -> TYPE
   * P'.type = TYPE.type; P'.width = TYPE.width;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule77(Node[] nodes, Node parent) {
    
    parent.addAttr("type", nodes[0].getAttr("type"));
    parent.addAttr("width", nodes[0].getAttr("width"));
  }
  
  /**
   * P''' -> P' IDN
   * enter(label, IDN, P'.type, P'.width);
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule80(Node[] nodes, Node parent) {
    String label = temps.get("label");
    
    enter(label, nodes[1].getValue(), nodes[0].getAttr("type"), Integer.valueOf(nodes[0].getAttr("width")));   // 函数声明已有参数声明，跳过
    
  }
  
  /**
   * U -> E' U'
   * U.num = U'.num + 1; U.code = gen( 'param' E'.addr ) || U'.code
   * 
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule81(Node[] nodes, Node parent) {
    
    int num = Integer.valueOf(nodes[1].getAttr("num")) + 1;
    parent.addAttr("num", String.valueOf(num));
    
    StringBuilder sb = new StringBuilder();
    
    String[] params = new String[] { "param", nodes[0].getAttr("addr") };
    sb.append(gen(params) + "#");
    sb.append(nodes[1].getAttr("code") + "#");
    
    String[] sentences = new String(sb).split("#");

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    for (int i = 0; i < sentences.length; i++) {

      ctrCodeLines.put(nextline, sentences[i]);
      
      nextline++;
    }

    temps.put("nextline", String.valueOf(nextline));
  }
  
  /**
   * U ->  
   * U' ->  
   * U.num = 0; U.code = '';
   * U'.num = 0; U'.code = '';
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule82(Node parent) {
    parent.addAttr("num", "0");
    parent.addAttr("code", "");
  }
  
  /**
   * U' -> , E' U'
   * U.num = U'1.num + 1; U'.code = gen( 'param' E'.addr ) || U'1.code
   * nextline += 1;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule83(Node[] nodes, Node parent) {
    
    int num = Integer.valueOf(nodes[2].getAttr("num")) + 1;
    parent.addAttr("num", String.valueOf(num));
    
    StringBuilder sb = new StringBuilder();
    //sb.append(nodes[1].getAttr("code") + "#");
    
    String[] params = new String[] { "param", nodes[1].getAttr("addr") };
    
    sb.append(gen(params) + "#");
    sb.append(nodes[2].getAttr("code") + "#");
    
    parent.addAttr("code", new String(sb));
    
  }
  
  /**
   * RELOP -> <
   * RELOP -> >
   * RELOP -> <=
   * RELOP -> >=
   * RELOP -> ==
   * RELOP -> !=
   * 
   * RELOP.val = sym;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule85(Node[] nodes, Node parent) {
    parent.addAttr("val", nodes[0].getSymbol());
  }
  
  /**
   * R -> E' RELOP E'
   * R.truelist = makelist(nextline); R.falselist = makelist(nextline + 1);
   * gen( 'if' E'1.addr RELOP.val E'2.addr 'goto @' );
   * gen( 'goto @' );
   * nextline += 2;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule91(Node[] nodes, Node parent) {
    
    int nextline = Integer.valueOf(temps.get("nextline"));
    
    parent.addAttr("truelist", makelist(nextline));
    parent.addAttr("falselist", makelist(nextline + 1));
    
    String[] params1 = new String[] { "if", nodes[0].getAttr("addr"), nodes[1].getAttr("val"), nodes[2].getAttr("addr"), "goto @" };
    String[] params2 = new String[] { "goto @" };

    ctrCodeLines.put(nextline, gen(params1));
    ctrCodeLines.put(nextline + 1, gen(params2));
    
    temps.put("nextline", String.valueOf(nextline + 2));
  }
  
  /**
   * R -> ( REL )
   * R.truelist = REL.truelist; R.falselist = REL.falselist;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule92(Node[] nodes, Node parent) {
    
    parent.addAttr("truelist", nodes[1].getAttr("truelist"));
    parent.addAttr("falselist", nodes[1].getAttr("falselist"));
    
  }
  
  /**
   * R -> E'
   * R.truelist = makelist(nextline); R.falselist = makelist(nextline + 1);
   * gen( 'if' E'.addr ' > 0 goto @' );
   * gen( 'goto @' );
   * nextline += 2;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule93(Node[] nodes, Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    parent.addAttr("truelist", makelist(nextline));
    parent.addAttr("falselist", makelist(nextline + 1));
    
    String[] params1 = new String[] { "if", nodes[0].getAttr("addr"), "> 0 goto @" };
    String[] params2 = new String[] { "goto @" };
    
    ctrCodeLines.put(nextline, gen(params1));
    ctrCodeLines.put(nextline + 1, gen(params2));
    
    temps.put("nextline", String.valueOf(nextline + 2));
  }
  
  /**
   * M2 ->  
   * M2.line = nextline;
   * 
   * @param parent
   * @param top
   */
  private void semanticRule94(Node parent) {
    parent.addAttr("line", temps.get("nextline"));
  }
  
  /**
   * BRCH -> IF ( REL ) M2 SENT CH
   * BRCH -> IF ( REL ) M2 { CONTS } CH
   * 
   * BRCH.nextlist = merge( REL.falselist, SENT.nextlist );
   * backpatch( REL.truelist, M2.line );
   * backpatch( REL.falselist, CH.line );
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule95(Node[] nodes, Node parent) {
    
    int loc = (nodes.length == 7) ? 6 : 7;
    
    String label = merge(nodes[1].getAttr("falselist"), nodes[loc].getAttr("nextlist"));
    
    parent.addAttr("nextlist", merge(label, nodes[nodes.length - 1].getAttr("nextlist")));
    
    backpatch(nodes[2].getAttr("truelist"), nodes[4].getAttr("line"));
    backpatch(nodes[2].getAttr("falselist"), nodes[nodes.length - 1].getAttr("line"));
    
    StringBuilder sb = new StringBuilder();
    
    sb.append(nodes[2].getAttr("code"));
    sb.append(nodes[loc].getAttr("code"));
    
    parent.addAttr("code", new String(sb));
  }
  
  /**
   * CH -> M6 ELSE M2 SENT
   * CH -> M6 ELSE M2 { CONTS }
   * 
   * CH.nextlist = M6.nextlist;
   * CH.line = M2.line;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule97(Node[] nodes, Node parent) {
    
    parent.addAttr("nextlist", nodes[0].getAttr("nextlist"));
    parent.addAttr("line", nodes[2].getAttr("line"));
    
  }
  
  /**
   * CH ->  
   * CH.nextlist = null; CH.line = nextline;
   * 
   * @param parent
   */
  private void semanticRule99(Node parent) {
    
    int codeline = Integer.valueOf(temps.get("nextline"));
    
    parent.addAttr("nextlist", "");
    parent.addAttr("line", String.valueOf(codeline));
  }
  
  /**
   * M6 ->  
   * M6.nextlist = makelist(nextline);
   * gen('goto @');
   * nextline += 1;
   * 
   * @param parent
   */
  private void semanticRule100(Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    parent.addAttr("nextlist", makelist(nextline));
    
    ctrCodeLines.put(nextline, gen(new String[] {"goto @"}));
    
    temps.put("nextline", String.valueOf(nextline + 1));
  }
  
  /**
   * CIR -> FOR ( E1 ; M2 E2 ; M2 E3 N ) M2 C
   * 
   * gen('goto' M2'.line);
   * nextline += 1;
   * CIR.nextlist = E2.falselist;
   * 
   * backpatch( C.nextlist, M2'.line );
   * backpatch( E3.nextlist, M2.line );
   * backpatch( N.nextlist, M2.line );
   * 
   * backpatch( E2.truelist, M2''.line );
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule101(Node[] nodes, Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    String[] params = new String[] {"goto", nodes[7].getAttr("line")};
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
    
    parent.addAttr("nextlist", nodes[5].getAttr("falselist"));
    
    backpatch(nodes[12].getAttr("nextlist"), nodes[7].getAttr("line"));
    backpatch(nodes[8].getAttr("nextlist"), nodes[4].getAttr("line"));
    backpatch(nodes[9].getAttr("nextlist"), nodes[4].getAttr("line"));
    
    backpatch(nodes[5].getAttr("truelist"), nodes[11].getAttr("line"));
    
  }
  
  /**
   * CIR -> WHILE M2 ( REL ) M2 C
   * 
   * gen('goto' M2.line);
   * nextline += 1;
   * CIR.nextlist = REL.falselist;
   * backpatch( C.nextlist, M2.line );
   * backpatch( REL.truelist, M2'.line );
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule102(Node[] nodes, Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    String[] params = new String[] { "goto", nodes[1].getAttr("line") };
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
    
    parent.addAttr("nextlist", nodes[3].getAttr("falselist"));
    
    backpatch(nodes[6].getAttr("nextlist"), nodes[1].getAttr("line"));
    backpatch(nodes[3].getAttr("truelist"), nodes[5].getAttr("line"));
  }
  
  /**
   * CIR -> DO M2 { CONTS } WHILE ( REL ) ;
   * 
   * CIR.nextlist = REL.falselist;
   * backpatch( REL.truelist, M2.line );
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule103(Node[] nodes, Node parent) {
    parent.addAttr("nextlist", nodes[8].getAttr("falselist"));
    
    backpatch(nodes[8].getAttr("truelist"), nodes[1].getAttr("line"));

  }
  
  /**
   * E2 -> REL
   * 
   * E2.truelist = REL.truelist;
   * E2.falselist = REL.falselist;
   * 
   * @param nodes
   * @param parent
   */
  private void semanticRule109(Node[] nodes, Node parent) {

    parent.addAttr("truelist", nodes[0].getAttr("truelist"));
    parent.addAttr("falselist", nodes[0].getAttr("falselist"));
  }
  
  /**
   * E2 ->  
   * 
   * gen( 'goto @' )
   * nextline += 1;
   * E2.truelist = makelist(nextline);
   * 
   * @param parent
   */
  private void semanticRule110(Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    String[] params = new String[] { "goto @" };
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
    
    parent.addAttr("truelist", makelist(nextline));
  }
  
  /**
   * N ->  
   * 
   * gen( 'goto @' );
   * nextline += 1;
   * E2.nextlist = makelist(nextline);
   * 
   * @param parent
   */
  private void semanticRule114(Node parent) {

    int nextline = Integer.valueOf(temps.get("nextline"));
    
    String[] params = new String[] { "goto @" };
    
    ctrCodeLines.put(nextline, gen(params));
    
    temps.put("nextline", String.valueOf(nextline + 1));
    
    parent.addAttr("nextlist", makelist(nextline));
  }
  
  private String array(String n, String type) {
    return "array(" + n + ", " + type + ")";
  }
  
  private void backpatch(String label, String codeline) {
    if (!codeline.equals("0")) {
      CtrlSet c = null;
      for (CtrlSet ctrlList : cSet) {
        if (ctrlList.getLabel().equals(label)) {
          c = ctrlList;
          break;
        }
      }
      
      Set<Integer> set = c.getLineSet();
      
      for (Integer integer : set) {
        String s = ctrCodeLines.get(integer);
        s = s.replace("@", codeline);
        ctrCodeLines.put(integer, s);
      }
    }
  }
  
  private boolean enter(String own, String id, String type, int width) {
    
    Record r = null;
    
    for (Record record : tblptr.keySet()) {
      if (record.getId().equals(own)) {
        r = record;
        break;
      }
    }
    
    if (r != null) {

      int offset = r.getOffset();
      Record newr = new Record(id, type, width, offset);
      
      List<Record> records = tblptr.get(r);
      if (!records.contains(newr)) {
        r.setOffset(offset + width);
        records.add(newr);
        tblptr.put(r, records);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
  
  private boolean enterLabel(String id, String type) {
    Record newr = new Record(id, type);
    
    if (tblptr.containsKey(newr)) {
      return false;
    } else {
      List<Record> records = new ArrayList<>();
      tblptr.put(newr, records);
      return true;
    }
  }
  
  /**
   * 生成表达式
   * 
   * @param params
   * @return
   */
  private String gen(String[] params) {
    StringBuilder sb1 = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    int len = params.length;
    
    for (int i = 0; i < len; i++) {
      sb1.append(params[i]);
      if (i > 1) {
        sb2.append(params[i]);
      }
      if (i != len - 1) {
        sb1.append(" ");
        if (i > 1) {
          sb2.append(" ");
        }
      }
    }
    
    if (temps.containsKey(params[0])) {   // 如果表达式左侧是临时变量，修改临时变量的值
      temps.put(params[0], new String(sb2));
    }
    
    return sb1 + " ;";
  }
  
  private Record lookup(String own, String id) {
    Record t = null;
    Record r = null;
    
    for (Record record : tblptr.keySet()) {
      if (record.getId().equals(own)) {
        r = record;
        break;
      }
    }
    
    if (r != null) {
      List<Record> records = tblptr.get(r);
      for (Record record : records) {
        if (record.getId().equals(id)) {
          t = record;
          break;
        }
      }
    }
    
    return t;
  }
  
  private Record lookupRecord(String own) {
    Record r = null;
    
    for (Record record : tblptr.keySet()) {
      if (record.getId().equals(own)) {
        r = record;
        break;
      }
    }
    
    return r;
  }
  
  private Record lookupProcess(String own, String[] subs) {
    Record r = null;
    
    for (Record record : tblptr.keySet()) {
      if (record.getId().equals(own)) {
        r = record;
        break;
      }
    }
    
    if (r != null) {
      List<Record> records = tblptr.get(r);
      if (records.size() == subs.length) {
        boolean equal = true;
        
        for (int i = 0; i < subs.length; i++) {
          String[] ptrs = subs[i].split("#");
          if (!ptrs[0].equals(records.get(i).getId()) || !ptrs[1].equals(records.get(i).getType())) {
            equal = false;
          }
        }
        
        if (equal) {  // 函数签名完全一致则认为是同一函数
          return r;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else {
      return r;
    }
  }
  
  private boolean match(String type1, String type2, boolean assign) {
    if (type1 == null || type2 == null) {
      return false;
    } else if (type1.equals(type2)) {   // 类型完全一致
      return true;
    } else if (assign && type2.equals("int") && (type1.equals("float") || type1.equals("double"))) {   // int和float、double
      return true;
    } else if (assign && type2.equals("float") && type1.equals("double")) {   // float和double
      return true;
    } else {
      if (!assign) {
        
      }
      return false;
    }
  }
  
  private String makelist(int codeline) {
    String label = "L" + numOfL++;
    
    CtrlSet c = new CtrlSet();
    Set<Integer> set = new HashSet<>();
    set.add(codeline);
    
    c.setLabel(label);
    c.setLineSet(set);
    cSet.add(c);
    
    return label;
  }
  
  private String merge(String label1, String label2) {
    CtrlSet c1 = null;
    CtrlSet c2 = null;
    CtrlSet c3 = new CtrlSet();
    int num = 0;
    
    for (CtrlSet c : cSet) {
      if (num > 1) {
        break;
      }
      
      if (c.getLabel().equals(label1)) {
        c1 = c;
        num++;
      } else if (c.getLabel().equals(label2)) {
        c2 = c;
        num++;
      }
    }
    
    String label = "L" + numOfL;
    numOfL++;
    
    c3.setLabel(label);
    
    Set<Integer> set = new HashSet<>();
    
    if (c1 != null) {
      set.addAll(c1.getLineSet());
    }
    if (c2 != null) {
      set.addAll(c2.getLineSet());
    }
    
    c3.setLineSet(set);
    cSet.add(c3);
    
    return label;
  }
  
  /**
   * 生成三地址码中需要的临时变量
   * 
   * @return
   */
  private String newtemp() {
    String newtemp = "t" + numOfT;
    numOfT++;
    
    temps.put(newtemp, "");
    return newtemp;
  }
}
