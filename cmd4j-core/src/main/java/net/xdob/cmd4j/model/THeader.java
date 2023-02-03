package net.xdob.cmd4j.model;

import org.jline.utils.WCWidth;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author yangzj
 * @date 2021/7/28
 */
public class THeader {
  public static final char PAD_CHAR = ' ';
  private final String name;
  private int width;
  private Align align = Align.right;

  public THeader(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    return width>=name.length()?width:name.length();
  }

  public THeader setWidth(int width) {
    this.width = width;
    return this;
  }

  public Align getAlign() {
    return align;
  }

  public THeader setAlign(Align align) {
    this.align = align;
    return this;
  }

  static int len(String s){
    int len = 0;
    for (char c : s.toCharArray()) {
      len += WCWidth.wcwidth(c);
    }
    return len;
  }

  public static void main(String[] args) {
    System.out.println("len = " + len("你好123"));
  }

  public static String padStart(String string, int minLength, char padChar) {
    checkNotNull(string); // eager for GWT.
    if (len(string) >= minLength) {
      return string;
    }
    StringBuilder sb = new StringBuilder(minLength);
    for (int i = len(string); i < minLength; i++) {
      sb.append(padChar);
    }
    sb.append(string);
    return sb.toString();
  }

  public static String padEnd(String string, int minLength, char padChar) {
    checkNotNull(string); // eager for GWT.
    if (len(string) >= minLength) {
      return string;
    }
    StringBuilder sb = new StringBuilder(minLength);
    sb.append(string);
    for (int i = len(string); i < minLength; i++) {
      sb.append(padChar);
    }
    return sb.toString();
  }

  public String padStart(String text){
    return padStart(text, getWidth(), PAD_CHAR);
  }

  public String padEnd(String text){
    return padEnd(text,getWidth(), PAD_CHAR);
  }

  public String padCenter(String text){
    String s = padEnd(text,getWidth()-(getWidth() - text.length())/2,PAD_CHAR);
    return padStart(s, getWidth(), PAD_CHAR);
  }

  public String pad(String text){
    if(Align.left.equals(align)){
      return padEnd(text);
    }else if(Align.right.equals(align)){
      return padStart(text);
    }
    return padCenter(text);
  }

  public String getHeader(){
    return pad(name);
  }

  @Override
  public String toString() {
    return getHeader();
  }

  public static THeader of(String name, int width, Align align){
    return new THeader(name).setWidth(width).setAlign(align);
  }

  public static THeader of(String name, int width){
    return of(name,width,Align.right);
  }

  public static THeader of(String name, Align align){
    return of(name,name.length(),align);
  }

  public static THeader of(String name){
    return of(name,name.length());
  }
}
