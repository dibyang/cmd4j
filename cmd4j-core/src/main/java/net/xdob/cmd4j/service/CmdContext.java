package net.xdob.cmd4j.service;



import net.xdob.cmd4j.model.CmdHelper;
import org.jline.reader.Completer;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public interface CmdContext {
  Cmd4jOut newT4mOut();
  void submit(Runnable runnable);
  void delay(int ms);
  void exitT4m();
  boolean isRunning();
  void setRunning(boolean running);
  String readLine();
  String readLine(String prompt);
  String readLine(String prompt, Character mask);
  String readLine(String prompt, Character mask, Completer completer);
  String readLine(String prompt, Predicate<String> validator);
  List<CmdHelper> getHelper(String cmd);
  AppContext getAppContext();
}
