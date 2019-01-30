package com.xr;

import com.xr.base.filter.AccessFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Arnold Yand
 * @summary 配置中心后台 相关配置
 * @time 2018/5/9 19:33
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

  @Bean
  public FilterRegistrationBean testFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new AccessFilter());
    registration.addUrlPatterns("/*");
    registration.setName("accessFilter");
    registration.setOrder(1);
    return registration;
  }

}
