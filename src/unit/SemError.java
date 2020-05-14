package unit;

public class SemError {
  
  private int line;
  private String description;
  
  public SemError(int line, String description) {
    this.line = line;
    this.description = description;
  }
  
  @Override
  public String toString() {
    return "Error at Line " + line + ": " + description;
  }
}
