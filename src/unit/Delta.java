package unit;

public class Delta {
  //private int instate;
  private String inchars;
  //private int outstate;
  
  public Delta(String inchars) {
    //this.instate = instate;
    this.inchars = inchars;
  }
  
  public String getInchars() {
    return inchars;
  }
  
  /**
   * 判断是否匹配
   * 
   * @param delta
   * @return
   */
  public boolean match(Delta delta) {
    if (delta == null) {
      return false;
    } else if (delta.inchars.matches("\\[.*\\]")) {
      boolean res = this.inchars.matches(delta.inchars);
      if (!delta.inchars.equals("[^/*]") && (this.inchars.equals("\n") || this.inchars.equals("\r"))) {
        res = false;
      }
      return res;
    } else {
      return false;
    }
    
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Delta) obj == this) {
      return true;
    } else if ((((Delta) obj).inchars.equals(this.inchars.toLowerCase())) || 
        (((Delta) obj).inchars.charAt(0) == '\\' && ((Delta) obj).inchars.charAt(1) == '\'' && this.inchars.equals("'")) ||
        (((Delta) obj).inchars.charAt(0) == '\\' && ((Delta) obj).inchars.charAt(1) == '"' && this.inchars.equals("\"")) ||
        (((Delta) obj).inchars.charAt(0) == '\\' && ((Delta) obj).inchars.charAt(1) == '\\' && this.inchars.equals("\\"))) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return inchars.toLowerCase().hashCode();
  }
  
  @Override
  public String toString() {
    return inchars;
  }
  
}
