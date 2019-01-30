package com.xr.config.core.properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.xr.config.core.enums.ResultStatus;
import com.xr.config.core.util.HttpUtils;
import com.xr.config.core.util.JSONUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义占位符替换
 *
 * 扩展自PropertyPlaceholderConfigurer
 *
 * 特性如下：
 * 1. 启动时 监控 动态config，并维护它们与相应bean的关系
 * 2. 当动态config变动时，此configurer会进行reload
 * 3. reload 时会 compare config value, and set value for beans
 *
 * @author Arnold Yang
 * @since 2018-04-27
 */
public class DefaultPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

  @Override
  protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
    props.putAll(loadAvailableConfig());
    super.processProperties(beanFactoryToProcess, props);
  }

  public Map<String, String> loadAvailableConfig() {
    try {
      String json = HttpUtils.get("http://" + config_server_host + "/load/available/env");
      JsonNode jsonNode = JSONUtil.parseObject(json);
      if(jsonNode.at("/code").asInt(ResultStatus.FAILED.getCode()) != ResultStatus.SUCCESS.getCode()){
        throw new RuntimeException("加载远程配置失败");
      }

      Map<String, String> configs = new HashMap<>();
      JsonNode jn = null;
      Iterator<JsonNode> it = jsonNode.at("/data").iterator();
      while (it.hasNext()) {
        jn = it.next();
        configs.put(jn.at("/key").asText(), jn.at("/value").asText());
      }

      return configs;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String config_server_host = "config.api.com";

  public String getConfig_server_host() {
    return config_server_host;
  }

  public void setConfig_server_host(String config_server_host) {
    this.config_server_host = config_server_host;
  }
}
