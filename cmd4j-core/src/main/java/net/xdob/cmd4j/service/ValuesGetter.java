package net.xdob.cmd4j.service;

import org.jline.reader.ParsedLine;

import java.util.List;

@FunctionalInterface
public interface ValuesGetter {
  void getValues(List<String> values, final ParsedLine commandLine);
}
