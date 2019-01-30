import com.xr.config.core.model.ConfigModel;
import com.xr.config.core.zk.ZookeeperManager;
import org.junit.Test;

import java.util.List;

/**
 * <p>
 * zookeeper测试
 * </p>
 *
 * @author Arnold Yang
 * @since 2018-04-18
 */
public class zkTest {

  @Test
  public void zk_test() throws Exception {
    ZookeeperManager manager = ZookeeperManager.getInstance();
    manager.init("127.0.0.1:2181", "");
    System.out.println("all children:\n" + manager.getRootChildren());
//    manager.writePersistent("/borrow/limit/max", "333");
//    manager.writePersistent("/borrow/limit/min", "222");
//    manager.writePersistent("/borrow/config/bank_depo", "666");
//    manager.writePersistent("/borrow/config/app_private", "555");

    List<ConfigModel> configs = manager.allChildren(null);
    for(ConfigModel cm : configs){
      System.out.println(cm.getKey() + ":" + cm.getValue());
    }


//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        try {
//          for(int i = 1;;i++){
//            manager.writePersistent("/borrow/limit/max", String.valueOf(i+i + 10));
//            manager.writePersistent("/borrow/limit/min", String.valueOf(i+i));
//            TimeUnit.SECONDS.sleep(11);
//          }
//        }catch (Exception e){
//
//        }
//      }
//    }).start();
//
//    while (true) {
//      System.out.println("limit_max="+manager.read("/borrow/limit/max"));
//      System.out.println("limit_min="+manager.read("/borrow/limit/min"));
//      TimeUnit.SECONDS.sleep(5);
//    }
  }

}
