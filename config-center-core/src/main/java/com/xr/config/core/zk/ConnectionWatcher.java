package com.xr.config.core.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * zookeeper连接管理
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-04-17
 */
public class ConnectionWatcher implements Watcher {

  protected static final Logger LOGGER = LoggerFactory.getLogger(ConnectionWatcher.class);

  protected static final int RETRY_PERIOD_IN_SECOND = 2;

  // 10 秒会话时间 ，避免频繁的session expired
  private static final int SESSION_TIMEOUT = 10000; // 毫秒

  private static final int CONNECT_TIMEOUT = 3000; // 毫秒

  protected ZooKeeper zk;
  private CountDownLatch connectedSignal = new CountDownLatch(1);

  // 保留一个根路径（可自定义），为了不与其他目录冲突
  public String currentRootPath = "/config";

  /**
   * zk地址列表（支持多个），如：127.0.0.1:2181,127.0.0.1:5181
   * 默认：127.0.0.1:2181
   */
  private static String zkHosts = "127.0.0.1:2181";

  public ConnectionWatcher() { }

  public ConnectionWatcher(String rootPath) {
    currentRootPath = (rootPath == null || rootPath.length() == 0) ? currentRootPath : rootPath;
  }

  /**
   * 创建zk连接
   *
   * @param hosts
   * @throws IOException
   * @throws InterruptedException
   */
  public void connect(String hosts) throws IOException, InterruptedException {
    zkHosts = (hosts == null || hosts.trim().length() == 0) ? zkHosts : hosts;
    zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);

    connectedSignal.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
    LOGGER.info("zookeeper: {} , connected.", zkHosts);
  }

  /**
   * 当连接成功时调用的
   */
  @Override
  public void process(WatchedEvent event) {
    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {

      LOGGER.info("zk sync connected");
      connectedSignal.countDown();

    } else if (event.getState().equals(Watcher.Event.KeeperState.Disconnected)) {

      // zookeeper挂了，等待zk起来
      LOGGER.warn("zk halted, waiting for startup.");

    } else if (event.getState().equals(Watcher.Event.KeeperState.Expired)) {

      // 会话丢失，重连
      LOGGER.info("zk session expired, waiting for reconnect...");
      reconnect();

    } else if (event.getState().equals(Watcher.Event.KeeperState.AuthFailed)) {

      // 无权限
      LOGGER.error("zk auth failed");

    }
  }

  /**
   * 重连，直至成功
   */
  public synchronized void reconnect() {

    LOGGER.info("start to reconnect....");

    int retries = 0;
    while (true) {

      try {
        if (!zk.getState().equals(ZooKeeper.States.CLOSED)) {
          break;
        }
        LOGGER.warn("zookeeper lost connection, reconnect...");

        close();
        connect(zkHosts);

      } catch (Exception e) {
        LOGGER.error("retry {} times : {}", retries, e.getMessage());

        // sleep then retry
        sleep(RETRY_PERIOD_IN_SECOND);
      }
    }
  }

  /**
   * 关闭zk连接
   *
   * @throws InterruptedException
   */
  public void close() throws InterruptedException {
    if(zk != null){
      zk.close();
    }
  }

  protected void sleep(int timeInSecond) {
    try {
      LOGGER.warn("Thread sleep {}s", timeInSecond);
      TimeUnit.SECONDS.sleep(timeInSecond);
    } catch (InterruptedException e1) {
      // Don't worries.
    }
  }

  protected String getRealPath(String path){
    if(path == null || path.length() == 0){
      return this.currentRootPath;
    }

    if(path.startsWith(this.currentRootPath)){
      return path;
    }

    return this.currentRootPath + path;
  }

  public ZooKeeper getZk() {
    return zk;
  }

  public void setZk(ZooKeeper zk) {
    this.zk = zk;
  }
}
