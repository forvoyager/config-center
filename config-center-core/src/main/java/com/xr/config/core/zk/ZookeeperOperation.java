package com.xr.config.core.zk;

import com.xr.config.core.model.ConfigModel;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * zookeeper读写操作
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-04-17
 */
public class ZookeeperOperation extends ConnectionWatcher {

  protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperOperation.class);

  protected static final Charset CHARSET = Charset.forName("UTF-8");

  protected static final int MAX_RETRIES = 3;

  public ZookeeperOperation(){}

  public ZookeeperOperation(String rootPath){
    super(rootPath);
  }

  /**
   * 创建根节点
   *
   * @param path
   * @param value
   * @throws InterruptedException
   * @throws KeeperException
   */
  public String createRootPath(String path, String value) throws InterruptedException, KeeperException {
    return createNode("", value, CreateMode.PERSISTENT);
  }

  /**
   * 往节点（不存在则创建）写入value，永久有效。
   *
   * @param path
   * @param value
   * @throws InterruptedException
   * @throws KeeperException
   */
  public String writePersistent(String path, String value) throws InterruptedException, KeeperException {
    return createNode(path, value, CreateMode.PERSISTENT);
  }

  /**
   * 创建Node（存在则更新）
   * zookeeper不支持多级目录级联创建
   * createNode封装了级联创建的过程：
   * 1、判断需要创建的节点是否存在。
   * 2、存在，则修改当前节点的数据为新值。
   * 3、不存在，则递归创建父节点。
   *
   * @param path
   * @param value
   * @param createMode
   * @return
   * @throws InterruptedException
   * @throws KeeperException
   */
  public String createNode(final String path, final String value, CreateMode createMode) throws InterruptedException, KeeperException {

    String finalPath = this.getRealPath(path);

    Stat stat = zk.exists(finalPath, false);

    // 不存在则创建
    if( stat == null) {
      String parentPath = finalPath.substring(0, finalPath.lastIndexOf("/"));
      // 存在父节点，则先创建父路径，再创建子路径
      if(parentPath.length() > 0) {
        createNode(parentPath, value, createMode);
        return zk.create(finalPath, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
      }
      // 否则创建子节点
      else {
        return zk.create(finalPath, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
      }
    }
    // 已存在，则更新节点数据
    else {
      if (value != null) {
        zk.setData(finalPath, value.getBytes(CHARSET), stat.getVersion());
      }
      return finalPath;
    }
  }

  /**
   * 判断节点是否存在
   *
   * @param path
   * @return
   * @throws InterruptedException
   * @throws KeeperException
   */
  public boolean exists(String path) throws InterruptedException, KeeperException {

    int retries = 0;
    while (true) {
      try {
        Stat stat = zk.exists(this.getRealPath(path), false);
        if (stat == null) {
          return false;
        } else {
          return true;
        }
      } catch (KeeperException e) {
        LOGGER.error("exists retry {} times(max:{}) : {}", retries, MAX_RETRIES, e.getMessage());
        if (retries++ == MAX_RETRIES) {
          LOGGER.error("connect final failed");
          throw e;
        }
        // sleep then retry
        sleep(RETRY_PERIOD_IN_SECOND * retries);
      }
    }
  }

  /**
   * 读取节点的数据
   *
   * @param path
   * @return
   * @throws InterruptedException
   * @throws KeeperException
   */
  public String read(String path) throws InterruptedException, KeeperException {
    byte[] data = zk.getData(this.getRealPath(path), false, null);
    return new String(data, CHARSET);
  }

  /**
   * 读取节点的数据
   *
   * @param path
   * @param watcher
   * @param stat
   * @return
   * @throws InterruptedException
   * @throws KeeperException
   */
  public String read(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {
    byte[] data = zk.getData(this.getRealPath(path), watcher, stat);
    return new String(data, CHARSET);
  }

  /**
   * 获取根路径下的所有节点
   *
   * @return
   */
  public List<String> getRootChildren() {
    List<String> children = new ArrayList<String>();

    try {
      children = zk.getChildren("/", false);
    } catch (Exception e) {
      LOGGER.error(e.toString());
    }

    return children;
  }

  /**
   * 获取路径下的所有节点
   *
   * @return
   */
  public List<ConfigModel> allChildren(String path) {
    if(path == null){
      path = this.currentRootPath;
    }

    List<ConfigModel> childrenConfigModel = new ArrayList<ConfigModel>();

    try {
      List<String> children = zk.getChildren(path, false);
      if(children == null || children.size() == 0){
        if( !this.currentRootPath.equals(path) ){
          childrenConfigModel.add(new ConfigModel(trimCurrentRootPath(path), read(path)));
        }
      } else {
        for(String child : children){
          childrenConfigModel.addAll( allChildren(path + "/" + child) );
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    return childrenConfigModel;
  }

  private String trimCurrentRootPath(String path){
    if(this.currentRootPath == null || this.currentRootPath.length() == 0){
      return path;
    }

    return path.replaceAll(this.currentRootPath, "");
  }
  /**
   * 删除节点
   *
   * @param path
   */
  public boolean deleteNode(String path) {
    try {
      zk.delete(this.getRealPath(path), -1);
      LOGGER.info("delete path {} success.", path);
      return true;
    } catch (Exception e) {
      LOGGER.error("delete path " + path + " failed.", e);
    }
    return false;
  }
}