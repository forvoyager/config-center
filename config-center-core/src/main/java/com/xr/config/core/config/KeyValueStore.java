package com.xr.config.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 配置信息
 * 本地存在一份配置信息，读取配置时从本地读取。
 * zookeeper watcher监控远程配置的变化，并更新本地配置。
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-04-17
 */
public class KeyValueStore {

  protected static final Logger LOGGER = LoggerFactory.getLogger(KeyValueStore.class);

  private KeyValueStore() { }

  // 配置信息
  private ConcurrentHashMap<String, ConfigItem> configItems = new ConcurrentHashMap<String, ConfigItem>();

  public ConfigItem getItem(String key) {
    if (configItems.containsKey(key)) {
      return configItems.get(key);
    }

    throw new IllegalArgumentException("can't find config info for : " + key);
  }

  public <T> T getValue(String key) {
    return (T)getItem(key).getValue();
  }

  public void saveItem(ConfigItem itm) {
    String key = itm.getKey();
    if (configItems.containsKey(key)) {
      LOGGER.error("There are two same item key : {}", key);
    } else {
      configItems.put(key, itm);
    }
  }

  public void deleteItem(String key) {
    if (configItems.containsKey(key)) {
      configItems.remove(key);
      LOGGER.info("");
    }
  }

  public ConcurrentHashMap<String, ConfigItem> getAllConfigItem() {
    return configItems;
  }

  /**
   * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
   */
  private static class SingletonHolder {
    /**
     * 静态初始化器，由JVM来保证线程安全
     */
    private static KeyValueStore instance = new KeyValueStore();
  }

  public static KeyValueStore getInstance() {
    return SingletonHolder.instance;
  }
}
