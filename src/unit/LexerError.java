package unit;

public class LexerError {
  private int line;
  private String content;
  
  public LexerError(int line, String content) {
    this.line = line;
    this.content = content;
  }
  
  @Override
  public String toString() {
    return "{line " + line + "}: " + content;
  }
}
