package unit;

public class Delta {
  private int instate;
  private char inchar;
  private int outstate;
  
  public Delta(int instate, char inchar) {
    this.instate = instate;
    this.inchar = inchar;
  }
  
  public void setOutstate(int outstate) {
    this.outstate = outstate;
  }
  
  public int getOutstate() {
    return outstate;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Delta) obj == this) {
      return true;
    } else if (((Delta) obj).instate == this.instate && ((Delta) obj).inchar == this.inchar) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return instate + inchar;
  }
  
  @Override
  public String toString() {
    return "(" + instate + "," + inchar + ") -> " + outstate;
  }
  
}
