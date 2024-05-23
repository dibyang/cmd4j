package net.xdob.cmd4j.boot;

import net.xdob.cmd4j.completer.CmdMgrCompleter;
import net.xdob.cmd4j.completer.ProxyCompleter;
import net.xdob.cmd4j.impl.Cmd4JOutImpl;
import net.xdob.cmd4j.model.OutColor;
import net.xdob.cmd4j.service.AppContext;
import net.xdob.cmd4j.service.CmdSupport;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;


public class CliCmd {

  public static final String APP_NAME = "app.name";
  private final String cliName;
  private final AppContext appContext;
  private final CmdSupport cmdSupport;

  public CliCmd(String cliName, AppContext appContext, CmdSupport cmdSupport) {
    this.cliName = cliName;
    this.appContext = appContext;
    this.cmdSupport = cmdSupport;
  }

  public CliCmd(AppContext appContext, CmdSupport cmdSupport)
  {
    this(System.getProperty(APP_NAME, "cli"), appContext, cmdSupport);
  }

  public void start(){
    appContext.submit(()->{
      try {
        doStart();
      } catch (IOException e) {
        //ignore
      }
    });
  }



  public void doStart() throws IOException {
    appContext.setPrompt(cliName + "#>");
    //构建系统终端
    Terminal terminal = TerminalBuilder.builder()
        .system(true)
        //.color(true)
        .build();

    //构建命令自动完成器
    ProxyCompleter proxyCompleter = new ProxyCompleter(new CmdMgrCompleter(
        cmdSupport
    ));
    cmdSupport.setProxyCompleter(proxyCompleter);
    //构建一个行输入读取器
    LineReader lineReader = LineReaderBuilder.builder()
        .terminal(terminal)
        .completer(proxyCompleter)
        .build();
    cmdSupport.setLineReader(lineReader);
    //命令提示符
    Cmd4JOutImpl cmd4JOut = new Cmd4JOutImpl(lineReader);
    //循环读取输入直到退出
    while (true) {
      try {
        cmd4JOut.print(OutColor.RESET);
        String line = lineReader.readLine(appContext.getPrompt());
        cmdSupport.doCommand(line);
      } catch (UserInterruptException e) {
        // Do nothing
        //System.out.println(e.getMessage());
      } catch (EndOfFileException e) {
        System.out.println("\nBye.");
        System.exit(0);
        return;
      }
    }
  }

}
