package net.xdob.cmd4j.completer;

import com.google.common.base.Strings;
import com.ls.luava.common.Types;
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
public class IntCompleter implements Completer {
  private int min;
  private int max=10;

  public int getMin() {
    return min;
  }

  public IntCompleter setMin(int min) {
    this.min = min;
    return this;
  }

  public IntCompleter setMin(String min) {
    if(!Strings.isNullOrEmpty(min)){
      setMin(Types.castToInt(min));
    }
    return this;
  }

  public int getMax() {
    return max;
  }

  public IntCompleter setMax(int max) {
    this.max = max;
    return this;
  }

  public IntCompleter setMax(String max) {
    if(!Strings.isNullOrEmpty(max)){
      setMax(Types.castToInt(max));
    }
    return this;
  }

  @Override
  public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
    int v = Types.cast(line.word(),Integer.class,min);
    String s = String.valueOf(v);
    candidates.add(new Candidate(AttributedString.stripAnsi(s), s, null, null, null, null, true));
    for(int i=0;i<10;i++) {
      int v2 = 10 * v + i;
      if(v2<=max) {
        s = String.valueOf(v2);
        candidates.add(new Candidate(AttributedString.stripAnsi(s), s, null, null, null, null, true));
      }else{
        break;
      }
    }

  }
}
