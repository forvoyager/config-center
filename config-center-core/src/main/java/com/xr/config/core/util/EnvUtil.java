package com.xr.config.core.util;

import com.xr.config.core.model.EnvModel;

import java.util.Map;

/**
 * @author Arnold Yand
 * @summary
 * @time 2018/7/7 18:16
 */
public final class EnvUtil {
  public static EnvModel get(String key) {
    return CacheUtil.get(key);
  }

  public static EnvModel getAvailable() {
    Map<String, EnvModel> allEnv = listAllWhenKeyLike(null);
    for(EnvModel env : allEnv.values()){
      if(env.isAvailable()){
        return env;
      }
    }

    throw new RuntimeException("没有可用的环境");
  }

  public static void put(String key, Object value) {
    CacheUtil.put(key, value);
  }

  public static void del(String key) {
    CacheUtil.del(key);
  }

  public static Map<String, EnvModel> listAllWhenKeyLike(String fuzzyKey) {
    return CacheUtil.listAllWhenKeyLike(fuzzyKey);
  }
}
