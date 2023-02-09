package net.xdob.cmd4j.service;

public interface AppContext {
  void setToken(String token);
  String getToken();
  String getSpace();
  void setSpace(String space);
  String getPrompt();
  void setPrompt(String prompt);
  <T> void setData(String key, T data);
  void removeData(String key);
  <T> T getData(String key, Class<T> clazz);
  void submit(Runnable runnable);
  void delay(int ms);
  void exit();
}
