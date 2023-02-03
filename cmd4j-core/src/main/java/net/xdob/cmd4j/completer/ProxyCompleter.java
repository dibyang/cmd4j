package net.xdob.cmd4j.completer;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

public class ProxyCompleter implements Completer {
  private volatile Completer completer;
  private final Completer old;

  public ProxyCompleter(Completer completer) {
    this.completer = completer;
    this.old = completer;
  }

  public Completer getCompleter() {
    return completer;
  }

  public void setCompleter(Completer completer) {
    this.completer = completer;
  }

  public void reset(){
    this.completer = old;
  }

  @Override
  public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
    if(completer!=null){
      completer.complete(reader, line, candidates);
    }
  }
}
