package net.xdob.cmd4j.completer;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.utils.AttributedString;

import java.util.List;

/**
 * @author yangzj
 * @date 2021/7/20
 */
public class DynamicCompleter implements Completer {
  private volatile Completer completer = null;
  private final String name;

  public String getName() {
    return name;
  }

  public DynamicCompleter(String name) {
    this(name,null);
  }

  public DynamicCompleter(String name, Completer completer) {
    this.name = name;
    this.completer = completer;
  }

  public Completer getCompleter() {
    return completer;
  }

  public void setCompleter(Completer completer) {
    this.completer = completer;
  }

  @Override
  public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
    if(completer==null) {
      candidates.add(new Candidate(AttributedString.stripAnsi(line.word()), line.word(), null, null, null, null, true));
    }else{
      completer.complete(reader,line,candidates);
    }
  }
}
