package net.xdob.cmd4j.model;

import com.ls.luava.common.Types2;

public class Option {
  private final String name;
  private String value;

  public Option(String name) {
    this(name,null);
  }

  public Option(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public <T> T getValue(Class<T> clazz,T defaultValue){
    return Types2.cast(value,clazz).orElse(defaultValue);
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return name + '=' + value;
  }

  public static Option of(String name, String value){
    return new Option(name,value);
  }
}
