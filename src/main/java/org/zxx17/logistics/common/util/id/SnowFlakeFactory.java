package org.zxx17.logistics.common.util.id;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.zxx17.logistics.constant.SnowFlakeConstants;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
public class SnowFlakeFactory {
  
  /**
   * 缓存SnowFlake对象.
   */
  private static final ConcurrentMap<String, SnowFlake> snowFlakeCache = new ConcurrentHashMap<>(2);

  public static SnowFlake getSnowFlake(long datacenterId, long machineId) {
    return new SnowFlake(datacenterId, machineId);
  }

  /**
   * 获取默认的SnowFlake对象.
   */
  public static SnowFlake getSnowFlake() {
    return new SnowFlake(SnowFlakeConstants.DEFAULT_DATACENTER_ID,
        SnowFlakeConstants.DEFAULT_MACHINE_ID);
  }

  /**
   * 获取缓存的SnowFlake对象.
   */
  public static SnowFlake getSnowFlakeFromCache() {
    SnowFlake snowFlake = snowFlakeCache.get(SnowFlakeConstants.DEFAULT_SNOW_FLAKE);
    if (snowFlake == null) {
      snowFlake = new SnowFlake(SnowFlakeConstants.DEFAULT_DATACENTER_ID,
          SnowFlakeConstants.DEFAULT_MACHINE_ID);
      snowFlakeCache.put(SnowFlakeConstants.DEFAULT_SNOW_FLAKE, snowFlake);
    }
    return snowFlake;
  }

  /**
   * 根据数据中心id和机器id从缓存中获取全局id.
   *
   * @param dataCenterId 取值为1~31
   * @param machineId   取值为1~31
   */
  public static SnowFlake getSnowFlakeByDataCenterIdAndMachineIdFromCache(
      Long dataCenterId, Long machineId
  ) {
    if (dataCenterId > SnowFlake.getMaxDataCenterNum() || dataCenterId < 0) {
      throw new IllegalArgumentException(
          "datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
    }
    if (machineId > SnowFlake.getMaxMachineNum() || machineId < 0) {
      throw new IllegalArgumentException(
          "machineId can't be greater than MAX_MACHINE_NUM or less than 0");
    }
    String key = SnowFlakeConstants.DEFAULT_SNOW_FLAKE
        .concat("_")
        .concat(String.valueOf(dataCenterId))
        .concat("_").concat(String.valueOf(machineId));
    SnowFlake snowFlake = snowFlakeCache.get(key);
    if (snowFlake == null) {
      snowFlake = new SnowFlake(dataCenterId, machineId);
      snowFlakeCache.put(key, snowFlake);
    }
    return snowFlake;
  }

}
