package net.xdob.cmd4j.model;

import com.google.common.collect.Lists;
import com.ls.luava.common.N3Map;


import java.util.List;

/**
 * @author yangzj
 * @date 2021/7/27
 */
public class Table {
  private final List<THeader> headers = Lists.newArrayList();
  private final List<String> cols = Lists.newArrayList();
  private final List<N3Map> rows = Lists.newArrayList();

  public List<THeader> getHeaders() {
    return headers;
  }

  public List<String> getCols() {
    return cols;
  }

  public List<N3Map> getRows() {
    return rows;
  }

  public Table add(String header,String col){
    headers.add(THeader.of(header));
    cols.add(col);
    return this;
  }

  public Table add(String header,String col,Align align){
    headers.add(THeader.of(header,align));
    cols.add(col);
    return this;
  }

  public Table add(String header,String col,int width){
    headers.add(THeader.of(header,width));
    cols.add(col);
    return this;
  }

  public Table add(String header,String col,int width,Align align){
    headers.add(THeader.of(header,width,align));
    cols.add(col);
    return this;
  }
}
