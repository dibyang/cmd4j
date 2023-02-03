package net.xdob.cmd4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yangzj
 * @date 2021/7/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd4jCmd {
  /**
   * 命令的帮助信息，描述命令的作用
   * @return
   */
  String value() default "";

  /**
   * 命令的使用举例
   * @return
   */
  String[] eg() default {};
}
