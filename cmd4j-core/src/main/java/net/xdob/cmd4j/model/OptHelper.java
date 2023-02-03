package net.xdob.cmd4j.model;

/**
 * @author yangzj
 * @date 2021/7/21
 */
public class OptHelper {
  private final String option;
  private String shortOption;
  private String desc;

  public String getOption() {
    return option;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getShortOption() {
    return shortOption;
  }

  public void setShortOption(String shortOption) {
    this.shortOption = shortOption;
  }

  public OptHelper(String option) {
    this.option = option;
  }
}
