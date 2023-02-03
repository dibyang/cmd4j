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
public @interface Cmd4jOption {
  String shortOption() default "";
  /**
   * 参数的描述信息
   * @return
   */
  String desc() default "";

  boolean hasValue() default false;

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
  String dynamic() default "";
  String min() default "";
  String max() default "";
}
