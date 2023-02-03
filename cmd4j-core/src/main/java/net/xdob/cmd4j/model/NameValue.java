package net.xdob.cmd4j.model;

public class NameValue {
  private final String name;
  private Object value;

  public NameValue(String name) {
    this(name,null);
  }

  public NameValue(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public static NameValue of(String name, Object value){
    return new NameValue(name,value);
  }

}
