package com.xr.config.core.zk;

import com.xr.config.core.model.ConfigModel;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>
 * zookeeper管理
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-04-17
 */
public class ZookeeperManager {

  protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperManager.class);

  private ZookeeperOperation store;

  private String currentHosts;

  public void init(String hosts, String rootPath) throws Exception {
    try {
      if(store != null){
        store.close();
      }
      store = new ZookeeperOperation(rootPath);
      store.connect(hosts);
      createRootPath(store.currentRootPath);
      currentHosts = hosts;
      LOGGER.info("Zookeeper Manager inited.");
    } catch (Exception e) {
      throw new Exception("zookeeper init failed. ", e);
    }
  }

  public String getCurrentHosts() {
    return currentHosts;
  }

  public void destroy(){
    try {
      store.close();
      LOGGER.info("zookeeper connection closed.");
    } catch (InterruptedException e) {
      LOGGER.error("close zookeeper connection failed.", e);
    }
  }

  private ZookeeperManager() { }

  private void createRootPath(String rootPath) {
    try {
      store.createRootPath(rootPath, String.valueOf(System.currentTimeMillis()));
      LOGGER.info("create root path {} success.", rootPath);
    } catch (Exception e) {
      LOGGER.error("create root path {} failed.", rootPath);
    }
  }

  public void reconnect() {
    store.reconnect();
  }

  public void release() throws InterruptedException {
    store.close();
  }

  public List<ConfigModel> allChildren(String path) {
    return store.allChildren(path);
  }

  public List<String> getRootChildren() {
    return store.getRootChildren();
  }

  public void writePersistent(String path, String value) throws Exception {
    store.writePersistent(path, value);
  }

  public String read(String path) throws Exception {
    if( path.startsWith("/") ){
      return store.read(path);
    }

    return store.read("/" + path);
  }

  public String read(String path, Watcher watcher) throws Exception {
    return store.read(path, watcher, null);
  }

  public String read(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {
    return store.read(path, watcher, stat);
  }

  public ZooKeeper getZk() {
    return store.getZk();
  }

  public boolean exists(String path) throws Exception {
    return store.exists(path);
  }

  public String createNode(String path, String value, CreateMode createMode) throws Exception {
    return store.createNode(path, value, createMode);
  }

  public void deleteNode(String path) {
    store.deleteNode(path);
  }

  private static class SingletonHolder {
    private static ZookeeperManager instance = new ZookeeperManager();
  }

  public static ZookeeperManager getInstance() {
    return SingletonHolder.instance;
  }
}
