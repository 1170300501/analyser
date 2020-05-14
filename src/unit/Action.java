package unit;

public class Action {
  private int former;
  private String terminal;
  private String action;
  
  public Action(int former, String terminal) {
    this.former = former;
    this.terminal = terminal;
  }
  
  public void setAction(String action) {
    this.action = action;
  }
  
  public int getFormer() {
    return former;
  }
  
  public String getTerminal() {
    return terminal;
  }
  
  public String getAction() {
    return action;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Action) obj == this) {
      return true;
    } else if (((Action) obj).former == this.former && ((Action) obj).terminal.equals(this.terminal)) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return former + terminal.hashCode();
  }
  
  @Override
  public String toString() {
    return "I" + former + " [ " + terminal + " ] " + action;
  }
}
