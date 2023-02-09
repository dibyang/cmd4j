package net.xdob.cmd4j.boot;

import net.xdob.cmd4j.impl.AppContextImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppContextImpl4Boot extends AppContextImpl {

  private final ConfigurableApplicationContext context;

  public AppContextImpl4Boot(ConfigurableApplicationContext context) {
    this.context = context;
  }

  @Override
  public void exit() {
    context.stop();
    super.exit();
  }
}
