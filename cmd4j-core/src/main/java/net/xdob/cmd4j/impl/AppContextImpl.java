package net.xdob.cmd4j.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import net.xdob.cmd4j.service.AppContext;

import java.util.Map;

public class AppContextImpl implements AppContext {
  public static final String USER_TOKEN = "user_token";
  private volatile String space;
  private volatile String prompt = "#>";
  private final Map<String,Object> map = Maps.newConcurrentMap();




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

}
