package net.xdob.cmd4j.completer;

import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.utils.AttributedString;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author yangzj
 * @date 2021/7/20
 */
public class StringsCompleter2  extends StringsCompleter {

  /**
   * 是否强制使用固定值，不可以使用其它值
   */
  private boolean forced;

  public boolean isForced() {
    return forced;
  }

  public StringsCompleter2 setForced(boolean forced) {
    this.forced = forced;
    return this;
  }


  public StringsCompleter2(Supplier<Collection<String>> stringsSupplier) {
    super(stringsSupplier);
  }

  public StringsCompleter2(String... strings) {
    super(strings);
  }

  public StringsCompleter2(Iterable<String> strings) {
    super(strings);
  }

  public StringsCompleter2(Candidate... candidates) {
    super(candidates);
  }

  public StringsCompleter2(Collection<Candidate> candidates) {
    super(candidates);
  }

  @Override
  public void complete(LineReader reader, final ParsedLine commandLine, final List<Candidate> candidates) {
    super.complete(reader, commandLine, candidates);
    if(!forced){
      final String word = commandLine.word().substring(0, commandLine.wordCursor());
      candidates.add(new Candidate(AttributedString.stripAnsi(word), word, null, null, null, null, true));
    }
  }

}
