
# cmd4j
支持快速构建命令行交互系统


## Sample Code for 


```java
//示例代码
//CliConfig 配置需要的bean
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

//DemoCli 启动spring boot应用
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

```
示例的扩展命令代码
```java
//HelloCmd
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
```

