package com.xr.config.core.annotation;


import java.lang.annotation.*;

/**
 * <p>
 * 配置项
 * 用于标记哪个配置从远程读取
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-03-25
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigItem {

  /**
   * KEY不能为空
   * 且必须保证 RemoteConfig.key[可选] + ConfigItem.key 全局唯一
   */
  String key();

  /**
   * 是否是重新加载才生效
   */
  boolean reload() default false;

  String version() default "";

}
