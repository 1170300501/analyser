package unit;

public class ParserError {
  
  private int line;
  private String description;
  
  public ParserError(int line, String description) {
    this.line = line;
    this.description = description;
  }
  
  @Override
  public String toString() {
    return "Error at Line " + line + ": " + description;
  }
}
