package unit;

import java.util.List;

public class Production {
  private String left;
  private String[] rights;
  private int toConven;
  private List<String[]> moves;
  
  public Production(String left, String[] rights, List<String[]> moves, int toConven) {
    this.left = left;
    this.rights = rights;
    this.moves = moves;
    this.toConven = toConven;
  }
  
  public String getLeft() {
    return left;
  }
  
  public String[] getRights() {
    return rights;
  }
  
  public List<String[]> getMoves() {
    return moves;
  }
  
  public int getToConven() {
    return toConven;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Production) obj == this) {
      return true;
    } else if (((Production) obj).left.equals(this.left) && ((Production) obj).toConven == this.toConven) {
      Production that = (Production) obj;
      if (that.rights.length != this.rights.length) { // 长度不相等则必定不一致
        return false;
      }
      boolean f = true;
      for (int i = 0; i < this.rights.length; i++) {
        if (!that.rights[i].equals(this.rights[i])) {
          f = false;
          break;
        }
      }
      return f;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return left.hashCode() + toConven;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(left + " -> ");
    for (int i = 0; i < rights.length + 1; i++) {
      if (i == toConven) {
        sb.append("·");
      }
      if (i != rights.length) {
        sb.append(rights[i] + " ");
      }
    }
    return sb + "";
  }
  
  public static void main(String[] args) {
    String[] a = new String[]{"a", "b"};
    String[] b = new String[]{"a", "b"};
    System.out.println(a[0].equals(b[0]));
  }
}
