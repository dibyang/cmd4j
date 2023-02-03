package net.xdob.cmd4j.service;


import net.xdob.cmd4j.completer.ProxyCompleter;
import net.xdob.cmd4j.model.CmdArg;
import net.xdob.cmd4j.model.CmdHelper;
import net.xdob.cmd4j.model.CmdOption;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;

import java.util.List;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public interface CmdSupport extends ValuesCompleterSupport {
  Completer getCmdCompleter();
  void handle(String line);
  List<CmdHelper> getHelper(String cmd, String space);
  List<Cmd> getCmds();
  Cmd getCmd(String name);
  List<CmdArg> getCmdArgs(Cmd cmd);
  List<CmdArg> getCmdArgs(String name);
  List<CmdOption> getAllOptions(Cmd cmd);
  List<CmdOption> getAllOptions(String name);
  List<Completer> getArgCompleters(String name);
  LineReader getLineReader();
  void setLineReader(LineReader lineReader);
  String getPrompt();
  ProxyCompleter getProxyCompleter();
  void setProxyCompleter(ProxyCompleter completer);
  ThreadLocal<CmdContext> getContextThreadLocal();
}
