package net.xdob.cmd4j.model;

/**
 * @author yangzj
 * @date 2021/7/21
 */
public class ArgHelper {
  private final String name;
  private String desc;

  public String getName() {
    return name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public ArgHelper(String name) {
    this.name = name;
  }
}
