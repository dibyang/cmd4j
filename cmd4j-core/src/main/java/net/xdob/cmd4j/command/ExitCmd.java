package net.xdob.cmd4j.command;


import net.xdob.cmd4j.annotation.Cmd4jCmd;
import net.xdob.cmd4j.service.CmdContext;

/**
 * @author yangzj
 * @date 2021/7/19
 */

@Cmd4jCmd(value = "It is used to exit.",eg={"exit","quit","q"})
public class ExitCmd extends BaseCmd{


  @Override
  public String name() {
    return "exit";
  }

  @Override
  public String[] alias() {
    return new String[]{"q","quit"};
  }

  @Override
  public void doCmd(CmdContext context) {
    context.exit();
  }

}
