package et.xdob.cli.cmd4j;

import net.xdob.cmd4j.boot.AppContextImpl4Boot;
import net.xdob.cmd4j.boot.CliCmd;
import net.xdob.cmd4j.boot.ServiceFactory4Boot;
import net.xdob.cmd4j.impl.CmdSupportService;
import net.xdob.cmd4j.service.AppContext;
import net.xdob.cmd4j.service.CmdSupport;
import net.xdob.cmd4j.service.ServiceFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CliConfig {

  @Bean
  public AppContext appContext(ConfigurableApplicationContext context,ServiceFactory serviceFactory){
    return new AppContextImpl4Boot(context, serviceFactory);
  }

  @Bean
  public CmdSupportService cmdSupportService(AppContext context,ServiceFactory serviceFactory){
    return new CmdSupportService(context);
  }

  @Bean
  public ServiceFactory serviceFactory(ConfigurableApplicationContext context){
    ServiceFactory4Boot serviceFactory = new ServiceFactory4Boot();
    serviceFactory.setApplicationContext(context);
    return serviceFactory;
  }

  @Bean
  public CliCmd cliCmd(AppContext appContext, CmdSupport cmdSupport){
    return new CliCmd(appContext, cmdSupport);
  }

}
