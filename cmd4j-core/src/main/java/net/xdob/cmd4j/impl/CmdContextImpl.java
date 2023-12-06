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

  public static final String INPUT_FORMAT_IS_NOT_VALID = "Input format is not valid.";
  public static final String Q_FOR_QUIT = "q for quit>";
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
    return readLine(Q_FOR_QUIT);
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
    String line = cmdSupport.getLineReader().readLine(prompt, mask).trim();
    cmdSupport.getProxyCompleter().reset();
    if(prompt.contains(Q_FOR_QUIT)) {
      running = !line.equalsIgnoreCase("q") && !line.equalsIgnoreCase("quit");

      if (!running) {
        return "";
      }
    }
    cmdSupport.getLineReader().printAbove("\033[1A");
    return line;
  }

  @Override
  public String readLine(String prompt, Predicate<String> predicate) {
    Validator<String> validator = v-> predicate.test(v)?null: INPUT_FORMAT_IS_NOT_VALID;
    return readLine2(prompt, validator);
  }

  @Override
  public String readLine(String prompt, Completer completer, Predicate<String> predicate) {
    Validator<String> validator = v-> predicate.test(v)?null: INPUT_FORMAT_IS_NOT_VALID;
    return readLine2(prompt, completer, validator);
  }

  @Override
  public String readLine(String prompt, String oldValue, Predicate<String> predicate) {
    Validator<String> validator = v-> predicate.test(v)?null: INPUT_FORMAT_IS_NOT_VALID;
    return readLine2(prompt, oldValue, validator);
  }

  @Override
  public String readLine2(String prompt, Validator<String> validator) {
    return readLine2(prompt,(Completer)null, validator);
  }

  @Override
  public String readLine2(String prompt, Completer completer, Validator<String> validator) {

    Cmd4jOut cmd4JOut = this.newT4mOut();
    String value = readLine(prompt,completer).trim();
    String error = null;
    while(!Strings.isNullOrEmpty(error= validator.valid(value))){
      cmd4JOut.println(error, OutColor.RED);
      value = readLine(prompt,completer);
    }
    return value;
  }

  @Override
  public String readLine2(String prompt, String oldValue, Validator<String> validator) {
    StringsCompleter completer = null;
    if(!Strings.isNullOrEmpty(oldValue)){
      completer = new StringsCompleter(oldValue);
    }
    return readLine2(prompt, completer, validator);
  }


  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt) {
    String line = readLine(prompt);
    return cast(line, clazz);
  }

  @Override
  public <T> Optional<T> readValue2(Class<T> clazz, String prompt, Validator<T> validator) {
    Validator<String> validator1 = v -> validator.valid(Types2.cast(v, clazz).orElse(null));
    String line = readLine2(prompt, validator1);
    return cast(line, clazz);
  }

  @Override
  public <T> Optional<T> readValue2(Class<T> clazz, String prompt, Completer completer, Validator<T> validator) {
    Validator<String> validator1 = v -> validator.valid(Types2.cast(v, clazz).orElse(null));
    String line = readLine2(prompt, completer, validator1);
    return cast(line, clazz);
  }

  @Override
  public <T> Optional<T> readValue2(Class<T> clazz, String prompt, T oldValue, Validator<T> validator) {
    Validator<String> validator1 = v -> validator.valid(Types2.cast(v, clazz).orElse(null));
    String line = readLine2(prompt, String.valueOf(oldValue), validator1);
    return cast(line, clazz);
  }

  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt, Predicate<T> predicate) {
    Validator<String> validator = v->
            predicate.test(cast(v, clazz).orElse(null))?null: INPUT_FORMAT_IS_NOT_VALID;
    String line = readLine2(prompt, validator);
    return cast(line, clazz);
  }

  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt, Completer completer, Predicate<T> predicate) {
    Validator<String> validator = v->
            predicate.test(cast(v, clazz).orElse(null))?null: INPUT_FORMAT_IS_NOT_VALID;
    String line = readLine2(prompt, completer, validator);
    return cast(line, clazz);
  }

  private <T> Optional<T> cast(String v, Class<T> clazz) {
    try {
      return Types2.cast(v, clazz);
    }catch (Exception e){
     return  Optional.empty();
    }
  }

  @Override
  public <T> Optional<T> readValue(Class<T> clazz, String prompt, T oldValue, Predicate<T> predicate) {
    Validator<String> validator = v->
            predicate.test(cast(v, clazz).orElse(null))?null: INPUT_FORMAT_IS_NOT_VALID;
    String line = readLine2(prompt, String.valueOf(oldValue), validator);
    return cast(line, clazz);
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
