package unit;

public class Delta {
  private int instate;
  private String inchars;
  private int outstate;
  
  public Delta(int instate, String inchars) {
    this.instate = instate;
    this.inchars = inchars;
  }
  
  public void setOutstate(int outstate) {
    this.outstate = outstate;
  }
  
  public int getOutstate() {
    return outstate;
  }
  
  /**
   * 判断是否匹配
   * 
   * @param delta
   * @return
   */
  public boolean match(Delta delta) {
    if (delta.instate == this.instate) {
      if (delta.inchars.equals("letter") && Character.isLetter(this.inchars.charAt(0))) {
        return true;
      } else if (delta.inchars.matches("others.*")) {
        String[] ptrs = delta.inchars.split("[\\(|\\)|\\s+]");
        boolean flag = true;
        
        for (int i = 0; i < ptrs.length; i++) {
          if (this.inchars.equals(ptrs[i])) {
            flag = false;
            break;
          }
        }
        return flag;
      } else if (delta.inchars.equals("\\s+") && this.inchars.matches(delta.inchars)) {
        return true;
      } else {
        return false;
      }
    }  else {
      return false;
    }
    
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Delta) obj == this) {
      return true;
    } else if (((Delta) obj).instate == this.instate && 
        ((((Delta) obj).inchars.equals(this.inchars.toLowerCase())) || 
        (((Delta) obj).inchars.charAt(0) == '\\' && ((Delta) obj).inchars.charAt(1) == '\'' && this.inchars.equals("'")) ||
        (((Delta) obj).inchars.charAt(0) == '\\' && ((Delta) obj).inchars.charAt(1) == '"' && this.inchars.equals("\"")) ||
        (((Delta) obj).inchars.charAt(0) == '\\' && ((Delta) obj).inchars.charAt(1) == '\\' && this.inchars.equals("\\")))) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return instate;
  }
  
  @Override
  public String toString() {
    return "(" + instate + "," + inchars + ") -> " + outstate;
  }
  
  
  public static void main(String[] args) {
    Delta delta1 = new Delta(0, " ");
    Delta delta2 = new Delta(0, "\n");
    System.out.println(delta2.match(delta1));
    String s1 = "\\s+";
    String s2 = "\n";
    System.out.println(s2.matches(s1));
  }
}
