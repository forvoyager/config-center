package com.xr.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Arnold Yand
 * @summary config center service application
 * @time 2018/5/9 19:53
 */
@SpringBootApplication(scanBasePackages = {"com.xr.start.config", "com.xr.config.api"})
@ImportResource("classpath:spring.xml")
public class ConfigCenterServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ConfigCenterServiceApplication.class, args);
  }
}
