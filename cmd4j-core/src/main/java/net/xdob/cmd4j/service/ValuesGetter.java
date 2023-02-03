package net.xdob.cmd4j.service;

import java.util.List;

@FunctionalInterface
public interface ValuesGetter {
  void getValues(List<String> values);
}
