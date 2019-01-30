package com.xr.config.service;

import com.xr.config.core.constant.Constants;
import com.xr.config.core.model.ConfigModel;
import com.xr.config.core.model.EnvModel;
import com.xr.config.core.service.IConfigService;
import com.xr.config.core.util.EnvUtil;
import com.xr.config.core.zk.ZookeeperManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author Arnold Yand
 * @summary 基于zookeeper的配置服务
 * @time 2018/6/17 14:23
 */
@Service("zkConfigService")
public class ZookeeperConfigServiceImpl implements IConfigService {

  private ZookeeperManager currentZkManager;

  public ZookeeperManager loadEnv(EnvModel env) throws Exception {
    if( currentZkManager == null || !currentZkManager.getCurrentHosts().equals(env.getUrl()) ){
      currentZkManager = ZookeeperManager.getInstance();
      currentZkManager.init(env.getUrl(), "");
    }
    return currentZkManager;
  }

  @Override
  public void disableEnv(String envId) throws Exception {
    EnvModel env = queryEnv(envId);
    env.setAvailable(false);
    if(currentZkManager != null && env.getUrl().equals(currentZkManager.getCurrentHosts())){
      currentZkManager.destroy();
    }
  }

  @Override
  public void enableEnv(String envId) throws Exception {
    EnvModel env = queryEnv(envId);
    env.setAvailable(true);
    if(currentZkManager != null ){
      currentZkManager.destroy();
    }
    loadEnv(env);
  }

  @Override
  public int addEnv(EnvModel env) throws Exception {
    EnvUtil.put(env.getEnvId(), env);
    return 1;
  }

  @Override
  public int deleteEnv(EnvModel env) throws Exception {
    EnvUtil.del(Constants.ENV_PREFIX + env.getDisplayName().hashCode());
    return 1;
  }

  @Override
  public EnvModel queryEnv(String envId) throws Exception {
    EnvModel em = EnvUtil.get(envId);
    Assert.notNull(em, "环境不存在");
    return em;
  }

  @Override
  public Map<String, EnvModel> listAllEnvWhenKeyLike(String fuzzyKey) throws Exception {
    return EnvUtil.listAllWhenKeyLike(fuzzyKey);
  }

  @Override
  public int addConfig(EnvModel env, ConfigModel config) throws Exception {
    env = queryEnv(env.getEnvId());
    loadEnv(env).writePersistent(parseToZookeeperPath(config.getKey()), config.getValue());
    return 1;
  }

  @Override
  public int deleteConfigByKey(EnvModel env, String key) throws Exception {
    env = queryEnv(env.getEnvId());
    loadEnv(env).deleteNode( parseToZookeeperPath(key) );
    return 1;
  }

  @Override
  public List<ConfigModel> queryAllConfig(EnvModel env) throws Exception {
    env = queryEnv(env.getEnvId());
    return parseZkPathToKey( loadEnv(env).allChildren(null) );
  }

  @Override
  public List<ConfigModel> queryAvailableConfig() throws Exception {
    EnvModel env = EnvUtil.getAvailable();
    if(env == null){
      return parseZkPathToKey( loadEnv(getLocalEnv()).allChildren(null));
    } else {
      return parseZkPathToKey( loadEnv(env).allChildren(null) );
    }
  }

  @Override
  public ConfigModel queryConfigByKey(EnvModel env, String key) throws Exception {
    env = queryEnv(env.getEnvId());
    return new ConfigModel(key, loadEnv(env).read( parseToZookeeperPath(key) ));
  }

  @PostConstruct
  public void init() throws Exception {
    // 添加一个本地的zookeeper环境，用于测试
    addEnv(getLocalEnv());
  }

  private EnvModel getLocalEnv() {
    EnvModel localEnv = new EnvModel();
    localEnv.setAvailable(true);
    localEnv.setDisplayName("localhost");
    localEnv.setUrl("127.0.0.1:2181");
    localEnv.setEnvId(Constants.ENV_PREFIX + localEnv.getDisplayName().hashCode());
    return localEnv;
  }

  private String parseToZookeeperPath(String key){
    return "/" + key.replaceAll("\\.", "/");
  }

  private List<ConfigModel> parseZkPathToKey(List<ConfigModel> configs){
    for(ConfigModel cm : configs){
      cm.setKey(parseZkPathToKey(cm.getKey()));
    }
    return configs;
  }

  private String parseZkPathToKey(String path){
    if(path==null || path.trim().length() == 0){
     return "";
    }

    return path.replace("/", ".").substring(1);
  }
}
