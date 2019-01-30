package com.xr.config.core.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arnold Yand
 * @summary 本地缓存操作
 * @time 2018/5/12 16:45
 */
public final class CacheUtil {

  // 本地缓存
  private static final Cache<String, Object> cache;

  static {
    cache = CacheBuilder.newBuilder()
            // 设置cache的初始大小为100
            .initialCapacity(100)
            // 设置并发数为3，即同一时间最多只能有3个线程往cache执行写入操作
            .concurrencyLevel(3)
            //设置cache中的数据在写入之后的存活时间为86400秒
            // .expireAfterWrite(86400, TimeUnit.SECONDS)
            // 构建cache实例
            .build();
  }

  public static <T> T get(String key) {
    return (T) cache.getIfPresent(key);
  }

  public static void put(String key, Object value) {
    cache.put(key, value);
  }

  public static void del(String key) {
    cache.invalidate(key);
  }

  public static <T> Map<String, T> listAllWhenKeyLike(String fuzzyKey) {
    Map<String, T> filterResult = new HashMap<>();
    for(String key : cache.asMap().keySet()){
      if(fuzzyKey == null || key.contains(fuzzyKey)){
        filterResult.put(key, (T)cache.getIfPresent(key));
      }
    }
    return filterResult;
  }
}
