package net.xdob.cmd4j.demo;

import net.xdob.cmd4j.completer.CmdMgrCompleter;
import net.xdob.cmd4j.completer.ProxyCompleter;
import net.xdob.cmd4j.impl.AppContextImpl;
import net.xdob.cmd4j.impl.CmdSupportService;
import net.xdob.cmd4j.impl.ServiceFactory4Spi;
import net.xdob.cmd4j.service.*;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
  static Logger LOG = LoggerFactory.getLogger(Main.class);
  private ServiceFactory serviceFactory = new ServiceFactory4Spi();

  private CmdSupport cmdSupport;

  private Terminal terminal = null;

  public void start() throws IOException {
    //构建系统终端
    terminal = TerminalBuilder.builder()
        .system(true)
        //.color(true)
        .build();
    AppContext appContext = new AppContextImpl();
    cmdSupport = new CmdSupportService(appContext, serviceFactory.getBeanList(ValuesGetterRegister.class), serviceFactory.getBeanList(Cmd.class));

    //构建命令自动完成器
    ProxyCompleter t4mCompleter = new ProxyCompleter(new CmdMgrCompleter(
        cmdSupport
    ));
    cmdSupport.setProxyCompleter(t4mCompleter);
    //构建一个行输入读取器
    LineReader lineReader = LineReaderBuilder.builder()
        .terminal(terminal)
        .completer(t4mCompleter)
        .build();
    cmdSupport.setLineReader(lineReader);
    //命令提示符

    //循环读取输入直到退出
    while (true) {
      try {
        String line = lineReader.readLine(cmdSupport.getPrompt());
        cmdSupport.handle(line);
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

  public static void main(String[] args) {
    Main main = new Main();
    try {
      main.start();
    } catch (IOException e) {
      LOG.error("",e);
    }
  }
}
