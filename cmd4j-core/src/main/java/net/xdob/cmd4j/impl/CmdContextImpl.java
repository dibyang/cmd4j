package net.xdob.cmd4j.impl;


import com.google.common.collect.Lists;
import net.xdob.cmd4j.model.OutColor;
import net.xdob.cmd4j.service.*;
import net.xdob.cmd4j.model.CmdHelper;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public class CmdContextImpl implements CmdContext {

  private volatile boolean running = true;

  private final ExecutorService executorService;
  private final CmdSupport cmdMgr;
  private final Cmd cmd;
  private final AppContext appContext;


  public CmdContextImpl(ExecutorService executorService, CmdSupport cmdMgr, Cmd cmd, AppContext appContext) {
    this.executorService = executorService;
    this.cmdMgr = cmdMgr;
    this.cmd = cmd;
    this.appContext = appContext;
  }

  @Override
  public Cmd4jOut newT4mOut() {
    return new Cmd4JOutImpl(cmdMgr.getLineReader());
  }

  @Override
  public void submit(Runnable runnable) {
    executorService.submit(runnable);
  }

  @Override
  public void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      //e.printStackTrace();
    }
  }

  @Override
  public void exitT4m() {
    Cmd4jOut out = newT4mOut();
    out.println("\nBye.");
    System.exit(0);
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
  public String readLine() {
    return readLine("q for quit>");
  }

  @Override
  public String readLine(String prompt) {
    return readLine(prompt,(Character)null);
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
      cmdMgr.getProxyCompleter().setCompleter(new ArgumentCompleter(list));

    }else{
      cmdMgr.getProxyCompleter().reset();
    }
    String line = cmdMgr.getLineReader().readLine(prompt, mask);
    cmdMgr.getProxyCompleter().reset();
    running=!line.equalsIgnoreCase("q")&&!line.equalsIgnoreCase("quit");
    if(!running){
      return "";
    }
    cmdMgr.getLineReader().printAbove("\033[1A");
    return line;
  }

  @Override
  public String readLine(String prompt, Predicate<String> validator) {
    Cmd4jOut cmd4JOut = this.newT4mOut();
    String value = readLine(prompt).trim();
    while(!validator.test(value)){
      cmd4JOut.println("Input format is not valid.", OutColor.RED);
      value = readLine(prompt);
    }
    return value;
  }

  @Override
  public List<CmdHelper> getHelper(String cmd) {
    return cmdMgr.getHelper(cmd,appContext.getSpace());
  }

  @Override
  public AppContext getAppContext() {
    return appContext;
  }


}
