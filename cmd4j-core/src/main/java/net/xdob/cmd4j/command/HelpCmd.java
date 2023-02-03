package net.xdob.cmd4j.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.xdob.cmd4j.annotation.Cmd4jArg;
import net.xdob.cmd4j.annotation.Cmd4jCmd;
import net.xdob.cmd4j.model.ArgHelper;
import net.xdob.cmd4j.model.CmdHelper;
import net.xdob.cmd4j.model.OptHelper;
import net.xdob.cmd4j.model.OutColor;
import net.xdob.cmd4j.service.Cmd;
import net.xdob.cmd4j.service.CmdContext;
import net.xdob.cmd4j.service.Cmd4jOut;


import java.util.List;

/**
 * @author yangzj
 * @date 2021/7/19
 */
@Cmd4jCmd(value = "It is used to get help.",eg={"help","help exit"})
public class HelpCmd extends BaseCmd{
  public static final Joiner JOINER = Joiner.on(",");
  @Cmd4jArg(value = "", desc ="Name of the command.", dynamic = Cmd.CMD)
  private String cmd;

  @Override
  public List<String> space() {
    return Lists.newArrayList(SPACE_ALL);
  }

  @Override
  public String name() {
    return "help";
  }


  @Override
  public String[] alias() {
    return new String[]{"h","?"};
  }

  @Override
  public void doCmd(CmdContext context) {
    Cmd4jOut out = context.newT4mOut();
    out.println("usage:");
    List<CmdHelper> helpers = context.getHelper(cmd);
    for (CmdHelper helper : helpers) {
      out.println("");
      out.println(helper.getDetail(), OutColor.GREEN);
      out.println(helper.getDesc());
      if(helper.getAlias().size()>0){
        out.printValueLn("alias", JOINER.join(helper.getAlias()));
      }
      if(helper.getOpts().size()>0) {
        out.println("options:");
        for (OptHelper opt : helper.getOpts()) {
          out.print("\t" + opt.getOption(), OutColor.GREEN);
          if(opt.getShortOption()!=null){
            out.print(", " + opt.getShortOption(), OutColor.GREEN);
          }
          out.println(": "+opt.getDesc(), OutColor.GREEN);
        }
      }
      if(helper.getArgs().size()>0) {
        out.println("args:");
        for (ArgHelper arg : helper.getArgs()) {
          out.println("\t" + arg.getName() + ": " + arg.getDesc(), OutColor.GREEN);
        }
      }
      out.println("e.g.");
      for (String eg : helper.getEg()) {
        out.println("\t"+eg, OutColor.GREEN);
      }

    }

  }
}
