package net.xdob.cmd4j.model;


import java.util.ArrayList;
import java.util.List;

/**
 * @author yangzj
 * @date 2021/7/21
 */
public class CmdHelper {
  private final String name;
  private final List<String> space = new ArrayList<>();
  private String detail;
  private String desc;
  private final List<String> alias = new ArrayList<>();
  private final List<String> eg = new ArrayList<>();
  private final List<ArgHelper> args = new ArrayList<>();
  private final List<OptHelper> opts = new ArrayList<>();

  public String getName() {
    return name;
  }

  public List<String> getSpace() {
    return space;
  }

  public void setSpace(List<String> space) {
    this.space.clear();
    if(space!=null){
      this.space.addAll(space);
    }
  }

  public List<String> getAlias() {
    return alias;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public List<String> getEg() {
    return eg;
  }

  public List<ArgHelper> getArgs() {
    return args;
  }

  public List<OptHelper> getOpts() {
    return opts;
  }

  public CmdHelper(String name, List<String> space) {
    this.name = name;
    this.setSpace(space);
  }
}
