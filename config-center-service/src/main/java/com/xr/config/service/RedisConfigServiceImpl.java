package com.xr.config.service;

import com.xr.config.core.model.ConfigModel;
import com.xr.config.core.model.EnvModel;
import com.xr.config.core.service.IConfigService;

import java.util.List;
import java.util.Map;

/**
 * @author Arnold Yand
 * @summary 基于redis的配置服务
 * @time 2018/6/17 14:49
 */
public class RedisConfigServiceImpl implements IConfigService {
  @Override
  public void disableEnv(String envId) throws Exception {
  }

  @Override
  public void enableEnv(String envId) throws Exception {
  }

  @Override
  public int addEnv(EnvModel env) throws Exception {
    return 0;
  }

  @Override
  public int deleteEnv(EnvModel env) throws Exception {
    return 0;
  }

  @Override
  public EnvModel queryEnv(String envId) throws Exception {
    return null;
  }

  @Override
  public Map<String, EnvModel> listAllEnvWhenKeyLike(String fuzzyKey) throws Exception {
    return null;
  }

  @Override
  public int addConfig(EnvModel env, ConfigModel config) throws Exception {
    return 0;
  }

  @Override
  public int deleteConfigByKey(EnvModel env, String key) throws Exception {
    return 0;
  }

  @Override
  public List<ConfigModel> queryAllConfig(EnvModel env) throws Exception {
    return null;
  }

  @Override
  public List<ConfigModel> queryAvailableConfig() throws Exception {
    return null;
  }

  @Override
  public ConfigModel queryConfigByKey(EnvModel env, String key) throws Exception {
    return null;
  }

}
