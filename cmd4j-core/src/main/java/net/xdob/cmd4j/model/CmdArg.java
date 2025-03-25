package net.xdob.cmd4j.model;

import com.ls.luava.i18n.Mapx;
import com.ls.luava.text.StrSubstitutor;
import net.xdob.cmd4j.completer.DynamicCompleter;


import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author yangzj
 * @date 2021/7/19
 */
public class CmdArg {
  private final String name;
  private String desc;
  private Class<?> type;
  private String value;
  private String[] values;
  private boolean required;
  private boolean forced;
  private String dynamic;
  private String file;
  private DynamicCompleter completer;

  public String getName() {
    return name;
  }

  public String getDesc() {
    Mapx mapx = new Mapx();
    if(values!=null&&values.length>0){
      mapx.put("values",values);
    }
    if(Enum.class.isAssignableFrom(type)){
      Class<? extends Enum<?>> source = (Class<? extends Enum<?>>)type;
      mapx.put("values", Arrays.stream(source.getEnumConstants()).map(e->e.name()).collect(Collectors.toList()));
    }
    return StrSubstitutor.replace(desc,mapx);
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public void setCompleter(DynamicCompleter completer) {
    this.completer = completer;
  }

  public Class<?> getType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String[] getValues() {
    return values;
  }

  public void setValues(String[] values) {
    this.values = values;
  }

  public boolean hasValues(){
    return values!=null&&values.length>0;
  }


  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean isForced() {
    return forced;
  }

  public void setForced(boolean forced) {
    this.forced = forced;
  }

  public String getDynamic() {
    return dynamic;
  }

  public void setDynamic(String dynamic) {
    this.dynamic = dynamic;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public DynamicCompleter getCompleter() {
    return completer;
  }

  public CmdArg(String name) {
    this.name = name;
    this.completer = new DynamicCompleter(name);
  }
}
