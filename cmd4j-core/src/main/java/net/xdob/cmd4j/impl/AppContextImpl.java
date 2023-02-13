package net.xdob.cmd4j.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ls.luava.os.OSProxy;
import com.ls.luava.os.OSProxyFactory;
import net.xdob.cmd4j.service.AppContext;
import net.xdob.cmd4j.service.ServiceFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AppContextImpl implements AppContext {
  public static final String USER_TOKEN = "user_token";
  private volatile String space;
  private volatile String prompt = "#>";
  private ServiceFactory serviceFactory;
  private final Map<String,Object> map = Maps.newConcurrentMap();

  final ExecutorService executorService = new ThreadPoolExecutor(1, 3,
      0L, TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<Runnable>());

  public AppContextImpl(ServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  @Override
  public void setToken(String token) {
    if(token==null){
      removeData(USER_TOKEN);
    }else {
      setData(USER_TOKEN, token);
    }
  }

  @Override
  public String getToken() {
    return getData(USER_TOKEN,String.class);
  }


  @Override
  public String getSpace() {
    return Strings.nullToEmpty(space);
  }

  @Override
  public void setSpace(String space) {
    this.space = space;
  }

  @Override
  public String getPrompt() {
    return prompt;
  }

  @Override
  public void setPrompt(String prompt) {
    this.prompt = Strings.nullToEmpty(prompt);
  }

  @Override
  public <T> void setData(String key, T data) {
    map.put(key,data);
  }

  @Override
  public void removeData(String key) {
    map.remove(key);
  }

  @Override
  public <T> T getData(String key, Class<T> clazz) {
    return (T)map.get(key);
  }

  @Override
  public void submit(Runnable runnable) {
    executorService.submit(runnable);
  }

  @Override
  public void delay(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      //e.printStackTrace();
    }
  }

  @Override
  public void exit() {
    executorService.shutdown();
    System.exit(0);
  }

  @Override
  public OSProxy getOSProxy() {
    return OSProxyFactory.factory.getOSProxy();
  }

  @Override
  public ServiceFactory getServiceFactory() {
    return serviceFactory;
  }

}
