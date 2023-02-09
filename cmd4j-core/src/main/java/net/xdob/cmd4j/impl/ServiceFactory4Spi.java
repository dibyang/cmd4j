package net.xdob.cmd4j.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.xdob.cmd4j.annotation.Name;
import net.xdob.cmd4j.service.ServiceFactory;

import java.util.*;

public class ServiceFactory4Spi  implements ServiceFactory {
  private final Map<Class<?>, Map<String,?>> provider = Maps.newConcurrentMap();

  public <T> T getBean(Class<T> type) {
    List<T> beans = getBeans(type);
    return beans.stream().findFirst().orElse(null);
  }

  public <T> List<T> getBeans(Class<T> type) {
    List<T> beans = Lists.newArrayList(getBeansOfType(type).values());
    beans.sort(beanComparator());
    return beans;
  }

  @Override
  public <T> Map<String, T> getBeansOfType(Class<T> type) {
    Map<String,T> beans = (Map<String,T>) provider.get(type);
    if (beans == null) {
      beans = Maps.newHashMap();
      ServiceLoader<T> serviceLoader = ServiceLoader.load(type);
      for (T bean : serviceLoader) {
        Class<?> beanClass = bean.getClass();
        Name name = beanClass.getAnnotation(Name.class);
        String beanName = name !=null? name.value():beanClass.getSimpleName();
        beans.put(beanName,bean);
      }
      provider.put(type, beans);
    }
    return beans;
  }

}
