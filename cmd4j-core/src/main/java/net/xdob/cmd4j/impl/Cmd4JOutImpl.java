package net.xdob.cmd4j.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.ls.luava.common.N3Map;
import com.ls.luava.i18n.Mapx;
import net.xdob.cmd4j.model.NameValue;
import net.xdob.cmd4j.model.OutColor;
import net.xdob.cmd4j.model.THeader;
import net.xdob.cmd4j.model.Table;
import net.xdob.cmd4j.service.Cmd4jOut;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public class Cmd4JOutImpl implements Cmd4jOut {
  static Logger LOG = LoggerFactory.getLogger(Cmd4jOut.class);
  protected final ReentrantLock lock = new ReentrantLock();

  private volatile int c = 0;
  private Splitter splitter = Splitter.on("\n");

  private LineReader lineReader;

  public Cmd4JOutImpl(LineReader lineReader) {
    this.lineReader = lineReader;
  }


  @Override
  public void printAbove(String s) {
    lineReader.printAbove(s);
  }

  @Override
  public void println(String s) {
    List<String> lines = splitter.splitToList(s);
    println(lines);
  }

  @Override
  public void print(OutColor color) {
    print(color.toString());
  }

  @Override
  public void print(String s, OutColor color) {
    print(color);
    print(s);
    print(OutColor.RESET);
  }

  @Override
  public void println(String s, OutColor color) {
    print(color);
    println(s);
    print(OutColor.RESET);
  }

  @Override
  public void printValueLn(String name, Object value) {
    print(name+": ");
    println(value!=null?value.toString():"", OutColor.BROWN);
  }

  @Override
  public void printValue(String name, Object value) {
    print(name+": ");
    print(value!=null?value.toString():"", OutColor.BROWN);
    print("\t");
  }

  @Override
  public void printValues(NameValue... values) {
    if(values!=null&&values.length>0){
      for (NameValue nameValue : values) {
        printValue(nameValue.getName(),nameValue.getValue());
      }
      print("\n");
    }
  }

  @Override
  public void print(String s) {
    try {
      lock.lock();
      Terminal terminal = lineReader.getTerminal();
      terminal.writer().print(s);
      terminal.flush();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public void println(List<String> lines) {
    c+=lines.size();
    printAbove(Joiner.on("\n\033[K").join(lines));
  }

  @Override
  public void markPos() {
    c = 0;
  }

  @Override
  public void resetPos() {
    if(c>0){
      upPos(c);
    }
    c = 0;
  }

  @Override
  public void upPos(int line) {
    if(line>0){
      printAbove("\033["+line+"A");
    }
  }

  @Override
  public void print(Table table) {
    final Joiner joiner = Joiner.on(" ");
    String headers = joiner.join(table.getHeaders());
    //List<String> lines = Lists.newArrayList();
    println(headers);
    for (N3Map row : table.getRows()) {
      Mapx mapx = new Mapx(row);
      List<String> line = Lists.newArrayList();
      for (int i = 0; i < table.getHeaders().size(); i++) {
        THeader header = table.getHeaders().get(i);
        String col = table.getCols().get(i);
        Object value = mapx.get(col);
        line.add(header.pad(value!=null?value.toString():""));
      }
      if(row.getBoolean("error").orElse(false)){
        println(joiner.join(line), OutColor.RED);
      }else if(row.getBoolean("active").orElse(false)){
        println(joiner.join(line), OutColor.GREEN);
      }else{
        println(joiner.join(line), OutColor.BROWN);
      }
    }
  }
}
