package net.xdob.cmd4j.command;

import com.google.common.collect.Lists;
import net.xdob.cmd4j.model.CmdArg;

import net.xdob.cmd4j.service.Cmd;
import net.xdob.cmd4j.service.CmdContext;
import net.xdob.cmd4j.service.CmdSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;


/**
 * @author yangzj
 * @date 2021/7/19
 */
public abstract class BaseCmd implements Cmd {

  protected static Logger LOG = LoggerFactory.getLogger(BaseCmd.class);
  private List<CmdArg> cmdArgs = null;


  //protected AppContext appContext;


  @Override
  public String[] alias() {
    return new String[0];
  }

  @Override
  public List<String> space() {
    return Lists.newArrayList("");
  }

  @Override
  public List<String> getAllNames() {
    List<String> names = Lists.newArrayList();
    names.add(name());
    final String[] alias = this.alias();
    if(alias!=null){
      for (String s : alias) {
        names.add(s);
      }
    }
    return names;
  }

  @Override
  public CmdArg getCmdArg(CmdSupport cmdMgr, String name){
    return cmdMgr.getCmdArgs(this).stream().filter(a->a.getName().equals(name))
      .findFirst().orElse(null);
  }


  public void preCmd(CmdContext context){

  }

  public void postCmd(CmdContext context){

  }


  protected void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
