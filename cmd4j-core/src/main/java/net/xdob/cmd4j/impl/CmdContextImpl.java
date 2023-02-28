package net.xdob.cmd4j.impl;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ls.luava.common.Types2;
import net.xdob.cmd4j.model.OutColor;
import net.xdob.cmd4j.service.*;
import net.xdob.cmd4j.model.CmdHelper;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public class CmdContextImpl implements CmdContext {

  private volatile boolean running = true;

  private final CmdSupport cmdSupport;
  private final AppContext appContext;


  public CmdContextImpl(CmdSupport cmdSupport, AppContext appContext) {
    this.cmdSupport = cmdSupport;
    this.appContext = appContext;
  }

  @Override
  public Cmd4jOut newT4mOut() {
    return new Cmd4JOutImpl(cmdSupport.getLineReader());
  }


  @Override
  public void exit() {
    Cmd4jOut out = newT4mOut();
    out.println("\nBye.");
    appContext.exit();

  }

  @Override
  public boolean isRunning() {
    return running;
  }

  @Override
  public void setRunning(boolean running) {
    this.running = running;
  }

  @Override
  public boolean confirm(String prompt,boolean defValue) {
    boolean confirm = defValue;
    String value = readLine(prompt,new StringsCompleter("yes","no")).trim();
    if("yes".equalsIgnoreCase(value)||"y".equalsIgnoreCase(value)){
      confirm = true;
    }
    if("no".equalsIgnoreCase(value)||"n".equalsIgnoreCase(value)){
      confirm = false;
    }
    return confirm;
  }

  @Override
  public String readLine() {
    return readLine("q for quit>");
  }

  @Override
  public String readLine(String prompt) {
    return readLine(prompt,(Character)null);
  }

  @Override
  public String readLine(String prompt, Completer completer) {
    return readLine(prompt, null, completer);
  }

  @Override
  public String readLine(String prompt, Character mask) {
    return readLine(prompt, mask, null);
  }

  @Override
  public String readLine(String prompt, Character mask, Completer completer) {
    if(completer!=null){
      List<Completer> list = Lists.newArrayList();
      list.add(completer);
      list.add(NullCompleter.INSTANCE);
      cmdSupport.getProxyCompleter().setCompleter(new ArgumentCompleter(list));
    }else{
      cmdSupport.getProxyCompleter().reset();
    }
    String line = cmdSupport.getLineReader().readLine(prompt, mask);
    cmdSupport.getProxyCompleter().reset();
    running=!line.equalsIgnoreCase("q")&&!line.equalsIgnoreCase("quit");

    if(!running){
      return "";
    }
    cmdSupport.getLineReader().printAbove("\033[1A");
    return line;
  }

  @Override
  public String readLine(String prompt, Predicate<String> validator) {
    return readLine(prompt,(Completer)null, validator);
  }

  @Override
  public String readLine(String prompt, Completer completer, Predicate<String> validator) {

    Cmd4jOut cmd4JOut = this.newT4mOut();
    String value = readLine(prompt,completer).trim();
    while(!validator.test(value)){
      cmd4JOut.println("Input format is not valid.", OutColor.RED);
      value = readLine(prompt,completer);
    }
    return value;
  }

  @Override
  public String readLine(String prompt, String oldValue, Predicate<String> validator) {
    StringsCompleter completer = null;
    if(!Strings.isNullOrEmpty(oldValue)){
      completer = new StringsCompleter(oldValue);
    }
    return readLine(prompt, completer, validator);
  }


  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt) {
    String line = readLine(prompt);
    return Types2.cast(line,clazz);
  }

  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt, Predicate<T> validator) {
    String line = readLine(prompt,v->validator.test(Types2.cast(v,clazz).orElse(null)));
    return Types2.cast(line,clazz);
  }

  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt, Completer completer, Predicate<T> validator) {
    String line = readLine(prompt, completer,v->validator.test(Types2.cast(v,clazz).orElse(null)));
    return Types2.cast(line,clazz);
  }

  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt, T oldValue, Predicate<T> validator) {
    String line = readLine(prompt, String.valueOf(oldValue), v->validator.test(Types2.cast(v,clazz).orElse(null)));
    return Types2.cast(line,clazz);
  }

  @Override
  public List<CmdHelper> getHelper(String cmd) {
    return cmdSupport.getHelper(cmd,appContext.getSpace());
  }

  @Override
  public AppContext getAppContext() {
    return appContext;
  }


}
