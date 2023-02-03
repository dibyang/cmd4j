package net.xdob.cmd4j.model;

import java.util.Objects;

public class OutColor {
  public static final OutColor RESET = c((short) 0);
  public static final OutColor RED = bright((short) 31);
  public static final OutColor GREEN = bright((short) 32);
  public static final OutColor BROWN = bright((short) 33);


  private boolean bright;
  private short color;

  private OutColor(boolean bright, short color) {
    this.bright = bright;
    this.color = color;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("\033[");
    if(color==(short) 0){
      builder.append("0m");
    }else {
      builder.append(bright ? "1" : "0")
          .append(";")
          .append(color)
          .append("m");
    }
    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OutColor outColor = (OutColor) o;
    return bright == outColor.bright && color == outColor.color;
  }

  @Override
  public int hashCode() {
    return Objects.hash(bright, color);
  }

  public static OutColor c(boolean bright, short color){
    return new OutColor(bright,color);
  }

  public static OutColor c(short color){
    return c(false,color);
  }

  public static OutColor bright(short color){
    return c(true,color);
  }
}
