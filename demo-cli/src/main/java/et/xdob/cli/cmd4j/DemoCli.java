package et.xdob.cli.cmd4j;

import net.xdob.cmd4j.boot.CliCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DemoCli {
  public static final String APP_NAME = "app.name";
  static Logger LOG = LoggerFactory.getLogger(DemoCli.class);

  private final CliCmd cliCmd;

  public DemoCli(CliCmd cliCmd) {
    this.cliCmd = cliCmd;
  }

  @PostConstruct
  public void start(){
    cliCmd.start();
  }

  public static void main(String[] args) {
    String appName = System.getProperty(APP_NAME, "demo-cli");
    System.setProperty(APP_NAME, appName);
    SpringApplicationBuilder builder = new SpringApplicationBuilder();
    builder.sources(DemoCli.class);
    builder.web(WebApplicationType.NONE);

    builder.application().setAllowBeanDefinitionOverriding(true);
    LOG.info(appName + " ready run.");
    ConfigurableApplicationContext context = null;
    try {
      context = builder.build().run();
    } catch (Exception e) {
      LOG.error(appName + " start fail.",e);
      if(context!=null){
        context.close ();
      }

    }
  }



}
