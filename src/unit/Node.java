package unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
  private String symbol;
  private String value;
  private Node parent;
  private Node[] children;
  private int line;
  private boolean epsilon;  // 是否空产生式的左部符号
  
  private Map<String, String> attrs;
  
  public Node(String symbol, String value, boolean epsilon) {
    this.symbol = symbol;
    this.value = value;
    this.epsilon = epsilon;
    
    attrs = new HashMap<>();
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }
  
  public void setChildren(Node[] children) {
    this.children = children;
  }
  
  public void setLine(int line) {
    this.line = line;
  }
  
  public void addAttr(String attr, String value) {
    attrs.put(attr, value);
  }
  
  public String getSymbol() {
    return symbol;
  }
  
  public String getValue() {
    return value;
  }
  
  public Node getParent() {
    return parent;
  }
  
  public Node[] getChildren() {
    return children;
  }
  
  public int getLine() {
    return line;
  }
  
  public boolean getEpsilon() {
    return epsilon;
  }
  
  public String getAttr(String attr) {
    return attrs.get(attr);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (value != null && !value.equals("_")) {
      sb.append(": " + value);
    }
    return symbol + sb + " (" + line + ") ";
  }
}
