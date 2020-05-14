package unit;

public class Token {
  private String key;
  private String value;
  private String word;
  private int line;
  
  public Token(String type) {
    this.key = type;
  }
  
  public void setValue(String value) {
    if (key.equals("IDN") || key.equals("NUMBER") || key.equals("STRING") || key.equals("CHA")) {
      this.value = value;
    }
  }
  
  public void setWord(String word) {
    this.word = word;
  }
  
  public void setLine(int line) {
    this.line = line;
  }
  
  public String getKey() {
    return key;
  }
  
  public String getValue() {
    return (value == null) ? "_" : value;
  }
  
  public String getWord() {
    return word;
  }
  
  public int getLine() {
    return line;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Token) obj == this) {
      return true;
    } else if (((Token) obj).key.equals(this.key) && ((Token) obj).value.equals(this.value)) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return key.hashCode() + value.hashCode();
  }
  
  @Override
  public String toString() {
    return "{ " + line + " } " + word + ": <" + key + ", " + value + ">";
  }
  
}
