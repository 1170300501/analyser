package unit;

public class Token {
  private String key;
  private String value;
  
  public Token(String type) {
    this.key = type;
  }
  
  public void setValue(String value) {
    if (key.equals("IDN") || key.equals("CONST") || key.equals("ERR")) {
      this.value = value;
    } else {
      this.value = "-";
    }
  }
  
  public String getKey() {
    return key;
  }
  
  public String getValue() {
    return value;
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
    return "<" + key + ", " + value + ">";
  }
}
