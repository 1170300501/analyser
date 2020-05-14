package unit;

import java.util.HashSet;
import java.util.Set;

public class CtrlSet {
  private String label;
  private Set<Integer> lineSet;
  
  public CtrlSet() {
    lineSet = new HashSet<>();
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public void setLineSet(Set<Integer> lineSet) {
    this.lineSet = lineSet;
  }
  
  public String getLabel() {
    return label;
  }
  
  public Set<Integer> getLineSet() {
    return lineSet;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if ((CtrlSet) obj == this) {
      return true;
    } else if (((CtrlSet) obj).label.equals(this.label) && ((CtrlSet) obj).lineSet.equals(this.lineSet)) {
      return true;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return label.hashCode() + lineSet.hashCode();
  }
  
  @Override
  public String toString() {
    return label + ": " + lineSet;
  }
}
