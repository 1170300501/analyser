package unit;

public class Record {
  private String id;    // 标识符
  private String type;  // 类型，对函数来说是返回值类型
  private int width;    // 占位大小
  private int offset;   // 位置偏移量
  
  public Record(String id, String type, int width, int offset) {
    this.id = id;
    this.type = type;
    this.width = width;
    this.offset = offset;
  }
  
  public Record(String id, String type) {
    this.id = id;
    this.type = type;
    this.width = 0;
    this.offset = 0;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public void setWidth(int width) {
    this.width = width;
  }
  
  public void setOffset(int offset) {
    this.offset = offset;
  }
  
  public String getId() {
    return id;
  }
  
  public String getType() {
    return type;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getOffset() {
    return offset;
  }
  
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((Record) obj == this) {
      return true;
    } else if (((Record) obj).id.equals(this.id) && ((Record) obj).type.equals(this.type) ) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return id.hashCode() + type.hashCode();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("|");
    
    int size1 = 14 - id.length();
    for (int i = 0; i < size1 / 2; i++) {
      sb.append(" ");
    }
    
    sb.append(id);
    
    for (int i = size1 / 2; i < size1; i++) {
      sb.append(" ");
    }

    sb.append("|");
    
    int size2 = 24 - type.length();
    for (int i = 0; i < size2 / 2; i++) {
      sb.append(" ");
    }
    
    sb.append(type);
    
    for (int i = size2 / 2; i < size2; i++) {
      sb.append(" ");
    }

    sb.append("|");
    
    sb.append(width + "\t|");
    sb.append(offset + "\t|");
    
    return sb + "";
  }
}
