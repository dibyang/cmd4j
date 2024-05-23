package et.xdob.cli.cmd4j.command;


import net.xdob.cmd4j.annotation.Cmd4jCmd;
import net.xdob.cmd4j.command.BaseCmd;
import net.xdob.cmd4j.service.Cmd4jOut;
import net.xdob.cmd4j.service.CmdContext;
import org.springframework.stereotype.Component;

@Component
@Cmd4jCmd(value = "say hello.",eg={"hello"})
public class HelloCmd extends BaseCmd {

  @Override
  public String name() {
    return "hello";
  }

  @Override
  public void doCmd(CmdContext context) {
    String name = context.readLine("what is your name?");
    Cmd4jOut cmd4jOut = context.newT4mOut();
    if(name!=null) {
      cmd4jOut.println("hello " + name + ".");
    }else{
      cmd4jOut.println("name can not be null");
    }
  }
}
