package net.xdob.cmd4j.model;


public class CmdOption extends CmdArg {
  private String shortOption;

  public String getOption() {
    return "--"+getName();
  }

  public String getShortOption() {
    if(shortOption!=null&&!shortOption.startsWith("-")) {
      return "-"+shortOption;
    }
    return shortOption;
  }

  public void setShortOption(String shortOption) {
    this.shortOption = shortOption;
  }

  public CmdOption(String name, String shortOption) {
    super(name);
    this.shortOption = shortOption;
  }

}
