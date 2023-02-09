package net.xdob.cmd4j.boot;

import net.xdob.cmd4j.impl.ServiceFactory4Spi;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class ServiceFactory4Boot extends ServiceFactory4Spi implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> type) {
    Map<String, T> beans = super.getBeansOfType(type);
    Map<String, T> beansOfType = applicationContext.getBeansOfType(type);
    beans.putAll(beansOfType);
    return beans;
  }
}
