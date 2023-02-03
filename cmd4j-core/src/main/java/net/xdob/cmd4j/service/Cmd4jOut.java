package net.xdob.cmd4j.service;



import net.xdob.cmd4j.model.NameValue;
import net.xdob.cmd4j.model.OutColor;
import net.xdob.cmd4j.model.Table;

import java.util.List;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public interface Cmd4jOut {
  void printAbove(String s);
  void printValueLn(String name, Object value);
  void printValue(String name, Object value);
  void printValues(NameValue... values);
  void print(String s);
  void println(String s);
  void print(OutColor color);
  void print(String s, OutColor color);
  void println(String s, OutColor color);

  void println(List<String> lines);
  void markPos();
  void resetPos();
  void upPos(int line);
  void print(Table table);
}
