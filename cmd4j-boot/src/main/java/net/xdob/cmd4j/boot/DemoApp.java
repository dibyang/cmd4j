package net.xdob.cmd4j.boot;

import net.xdob.cmd4j.completer.CmdMgrCompleter;
import net.xdob.cmd4j.completer.ProxyCompleter;
import net.xdob.cmd4j.impl.AppContextImpl;
import net.xdob.cmd4j.impl.CmdSupportService;
import net.xdob.cmd4j.service.*;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;

import javax.annotation.PostConstruct;
import javax.smartcardio.TerminalFactory;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class DemoApp {
  static Logger LOG = LoggerFactory.getLogger(DemoApp.class);
  static ConfigurableApplicationContext context;
  private ServiceFactory serviceFactory;
  private AppContext appContext;

  private CmdSupport cmdSupport;

  private Terminal terminal = null;

  @Autowired
  public void setAppContext(AppContext appContext) {
    this.appContext = appContext;
  }

  @Bean
  public ServiceFactory serviceFactory(){
    if(serviceFactory==null) {
      serviceFactory = new ServiceFactory4Boot();
    }
    return serviceFactory;
  }

  @PostConstruct
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
    //构建系统终端
    terminal = TerminalBuilder.builder()
        .system(true)
        //.color(true)
        .build();
    //AppContext appContext = new AppContextImpl4Boot(context);
    cmdSupport = new CmdSupportService(appContext);

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

    //循环读取输入直到退出
    while (true) {
      try {
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

  public static void main(String[] args) {

    SpringApplicationBuilder builder = new SpringApplicationBuilder();
    builder.sources(DemoApp.class);
    builder.application().setAllowBeanDefinitionOverriding(true);
    LOG.info("spring ready run.");

    try {
      context = builder.build().run();

    } catch (Exception e) {
      e.printStackTrace();
      LOG.error("spring start fail.",e);
      if(context!=null){
        context.close ();
      }

    }
    addHook((AbstractApplicationContext)context);
  }


  private static final ReentrantLock LOCK = new ReentrantLock();
  private static final Condition STOP = LOCK.newCondition();

  private static void addHook(AbstractApplicationContext applicationContext) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        applicationContext.stop();
      } catch (Exception e) {
        LOG.error("StartMain stop exception ", e);
      }

      LOG.info("jvm exit, all service stopped.");
      LOCK.lock();
      try {
        STOP.signal();
      } finally {
        LOCK.unlock();
      }
    }, "StartMain-shutdown-hook"));
  }

}
