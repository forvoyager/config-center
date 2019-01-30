package com.xr.config.core.service;

import com.xr.config.core.model.ConfigModel;
import com.xr.config.core.model.EnvModel;

import java.util.List;
import java.util.Map;

/**
 * @author Arnold Yand
 * @summary 配置服务
 * @time 2018/6/17 14:14
 */
public interface IConfigService {

  /**
   * 禁用环境
   * @param envId
   * @return
   * @throws Exception
   */
  void disableEnv(String envId) throws Exception;

  /**
   * 启用环境
   * @param envId
   * @return
   * @throws Exception
   */
  void enableEnv(String envId) throws Exception;

  /**
   * 新增环境
   *
   * @param env
   * @return
   * @throws Exception
   */
  int addEnv(EnvModel env) throws Exception;

  /**
   * 删除环境
   *
   * @param env
   * @return
   * @throws Exception
   */
  int deleteEnv(EnvModel env) throws Exception;

  /**
   * 查询环境
   *
   * @param envId
   * @return
   * @throws Exception
   */
  EnvModel queryEnv(String envId) throws Exception;

  /**
   * 查询所有环境
   *
   * @param fuzzyKey
   * @return
   * @throws Exception
   */
  Map<String, EnvModel> listAllEnvWhenKeyLike(String fuzzyKey) throws Exception;

  /**
   * 新增配置项
   *
   * @param env
   * @param config
   * @throws Exception
   */
  int addConfig(EnvModel env, ConfigModel config) throws Exception;


  /**
   * 删除配置项
   *
   * @param env
   * @param key
   * @return
   * @throws Exception
   */
  int deleteConfigByKey(EnvModel env, String key) throws Exception;

  /**
   * 查询所有配置项
   *
   * @param env
   * @return
   * @throws Exception
   */
  List<ConfigModel> queryAllConfig(EnvModel env) throws Exception;

  /**
   * 查询可用配置项
   *
   * @return
   * @throws Exception
   */
  List<ConfigModel> queryAvailableConfig() throws Exception;

  /**
   * 查询指定配置项
   *
   * @param env
   * @param key
   * @return
   * @throws Exception
   */
  ConfigModel queryConfigByKey(EnvModel env, String key) throws Exception;
}
