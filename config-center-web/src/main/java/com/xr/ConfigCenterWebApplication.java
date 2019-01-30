package com.xr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Arnold Yand
 * @summary config center application
 * @time 2018/5/9 19:53
 */
@SpringBootApplication(scanBasePackages = {"com.xr.start.config", "com.xr.ccw"})
@ImportResource("classpath:spring.xml")
public class ConfigCenterWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigCenterWebApplication.class, args);
  }
}
