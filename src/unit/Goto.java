package unit;

public class Goto {
  private int former;
  private String symbol;
  private int latter;
  
  public Goto(int former, String symbol) {
    this.former = former;
    this.symbol = symbol;
  }
  
  public void setLatter(int latter) {
    this.latter = latter;
  }
  
  public int getFormer() {
    return former;
  }
  
  public String getSymbol() {
    return symbol;
  }
  
  public int getLatter() {
    return latter;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Goto) obj == this) {
      return true;
    } else if (((Goto) obj).former == this.former && ((Goto) obj).symbol.equals(this.symbol)) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return former + symbol.hashCode();
  }
  
  @Override
  public String toString() {
    return "[ " + symbol + " ] : I" + former + " -> " + "I" + latter;
  }
}
