package net.xdob.cmd4j.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.xdob.cmd4j.service.ServiceFactory;

import java.util.*;

public class ServiceFactory4Spi implements ServiceFactory {
  private final Map<Class<?>, List<?>> provider = Maps.newConcurrentMap();

  @Override
  public <T> T getBean(Class<T> clazz) {
    List<T> beans = getBeans(clazz);
    return beans.stream().findFirst().orElse(null);
  }

  private <T> List<T> getBeans(Class<T> clazz) {
    List<T> beans = (List<T>) provider.get(clazz);
    if (beans == null) {
      beans = Lists.newArrayList();
      ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
      for (T bean : serviceLoader) {
        beans.add(bean);
      }
      beans.sort(beanComparator());
      provider.put(clazz, beans);
    }
    return beans;
  }

  @Override
  public <T> List<T> getBeanList(Class<T> clazz) {
    List<T> beans = getBeans(clazz);
    return beans;
  }
}
