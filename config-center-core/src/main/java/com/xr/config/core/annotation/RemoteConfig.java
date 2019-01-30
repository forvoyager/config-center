package com.xr.config.core.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 远程配置
 * 表示此注解标记的类中@ConfigItem对应的数据从远程读取
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-03-25
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//@Scope("singleton")
public @interface RemoteConfig {
  /**
   * KEY不能为空
   * 且必须保证 RemoteConfig.key[可选] + ConfigItem.key 全局唯一
   */
  String key();

  String env() default "";

  String path() default "";

  String version() default "";

}
