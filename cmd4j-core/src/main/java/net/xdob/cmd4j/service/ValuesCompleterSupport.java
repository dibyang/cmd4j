package net.xdob.cmd4j.service;

import net.xdob.cmd4j.completer.ValuesCompleter;

public interface ValuesCompleterSupport {
  ValuesCompleter getValuesCompleter(String name);
}
