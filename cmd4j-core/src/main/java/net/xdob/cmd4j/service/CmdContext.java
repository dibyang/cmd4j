package net.xdob.cmd4j.service;



import net.xdob.cmd4j.model.CmdHelper;
import org.jline.reader.Completer;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public interface CmdContext {
  Cmd4jOut newT4mOut();
  void exit();
  boolean isRunning();
  void setRunning(boolean running);
  boolean confirm(String prompt,boolean defValue);
  String readLine();
  String readLine(String prompt);
  String readLine(String prompt, Completer completer);
  String readLine(String prompt, Character mask);
  String readLine(String prompt, Character mask, Completer completer);

  String readLine2(String prompt, Validator<String> validator);
  String readLine2(String prompt, Completer completer, Validator<String> validator);
  String readLine2(String prompt, String oldValue, Validator<String> validator);

  @Deprecated
  String readLine(String prompt, Predicate<String> predicate);
  @Deprecated
  String readLine(String prompt, Completer completer, Predicate<String> predicate);
  @Deprecated
  String readLine(String prompt, String oldValue, Predicate<String> predicate);



  <T> Optional<T> readValue(Class<T> clazz, String prompt);

  <T> Optional<T> readValue2(Class<T> clazz, String prompt, Validator<T> validator);
  <T> Optional<T> readValue2(Class<T> clazz, String prompt, Completer completer, Validator<T> validator);
  <T> Optional<T> readValue2(Class<T> clazz, String prompt, T oldValue, Validator<T> validator);

  @Deprecated
  <T> Optional<T> readValue(Class<T> clazz, String prompt, Predicate<T> predicate);
  @Deprecated
  <T> Optional<T> readValue(Class<T> clazz, String prompt, Completer completer, Predicate<T> predicate);
  @Deprecated
  <T> Optional<T> readValue(Class<T> clazz, String prompt, T oldValue, Predicate<T> predicate);

  List<CmdHelper> getHelper(String cmd);
  AppContext getAppContext();
}
