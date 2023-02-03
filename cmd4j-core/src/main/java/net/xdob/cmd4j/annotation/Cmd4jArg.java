package net.xdob.cmd4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author admin
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd4jArg {
  /**
   * 参数的描述信息
   * @return
   */
  String desc() default "";

  /**
   * 默认值
   * @return
   */
  String value() default "";
  /**
   * 可选的值
   * @return
   */
  String[] values() default {};

  /**
   * 是否是必须的参数，不可省略。通常是false，缺省使用默认值
   * @return
   */
  boolean required() default false;

  /**
   * 是否强制使用固定值，不可以使用其它值
   * @return
   */
  boolean forced() default false;

  /**
   * 动态自动完成的名称
   * @return
   */
  String dynamic() default "";

  String min() default "";
  String max() default "";

}
