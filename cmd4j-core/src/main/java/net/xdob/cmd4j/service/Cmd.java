package net.xdob.cmd4j.service;


import net.xdob.cmd4j.model.CmdArg;

import java.util.List;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public interface Cmd {

  String SPACE_ALL = "_all";
  String CMD = "cmd";
  String name();
  List<String> space();
  String[] alias();
  List<String> getAllNames();
  CmdArg getCmdArg(CmdSupport cmdMgr, String name);
  void preCmd(CmdContext context);
  void doCmd(CmdContext context);
  void postCmd(CmdContext context);
}
