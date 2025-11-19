package net.xdob.cmd4j.completer;

import net.xdob.cmd4j.service.ValuesGetter;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.utils.AttributedString;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ValuesCompleter extends StringsCompleter {
  private final String name;
  private final ValuesGetter getter;

  public ValuesCompleter(String name, ValuesGetter getter) {
    this.name = name;
    this.getter = getter;
  }

  public String getName() {
    return name;
  }

  @Override
  public void complete(LineReader reader, final ParsedLine commandLine, final List<Candidate> candidates) {
    candidates.clear();
    List<String> values = new ArrayList<>();
    getter.getValues(values, commandLine);
    for (String value : values) {
      candidates.add(new Candidate(AttributedString.stripAnsi(value), value, null, null, null, null, true));
    }
    super.complete(reader, commandLine, candidates);
  }
}
