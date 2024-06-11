package org.zxx17.logistics.common.util.id;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.constant.SnowFlakeConstants;

/**
 * SnowFlake算法的实现，用于生成全局唯一ID. 该算法基于时间戳、数据中心ID、机器ID和序列号的组合生成一个64位的ID. 能够保证在分布式系统中的唯一性且趋势递增.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
public class SnowFlake {

  private final long datacenterId;
  private final long machineId;
  private long sequence = 0L;
  private long lastStamp = -1L;

  /**
   * 用于线程同步的锁.
   */
  private final Lock lock = new ReentrantLock();

  /**
   * 构造函数，初始化SnowFlake实例.
   *
   * @param datacenterId 数据中心ID
   * @param machineId    机器ID
   * @throws IllegalArgumentException 如果数据中心ID或机器ID超出允许范围
   */
  public SnowFlake(long datacenterId, long machineId) {
    if (datacenterId > SnowFlakeConstants.MAX_DATACENTER_NUM || datacenterId < 0) {
      throw new IllegalArgumentException(
          "数据中心ID必须在0到" + SnowFlakeConstants.MAX_DATACENTER_NUM + "之间");
    }
    if (machineId > SnowFlakeConstants.MAX_MACHINE_NUM || machineId < 0) {
      throw new IllegalArgumentException(
          "机器ID必须在0到" + SnowFlakeConstants.MAX_MACHINE_NUM + "之间");
    }
    this.datacenterId = datacenterId;
    this.machineId = machineId;
  }

  /**
   * 生成下一个ID. 在高并发环境下，通过锁机制保证ID的唯一性和递增性.
   *
   * @return 下一个ID
   * @throws IllegalStateException 如果检测到时钟回拨
   */
  public long nextId() {
    lock.lock();
    try {
      long currStamp = getNewStamp();
      if (currStamp < lastStamp) {
        throw new IllegalStateException("检测到时钟回拨，拒绝生成ID");
      }

      if (currStamp == lastStamp) {
        sequence = (sequence + 1) & SnowFlakeConstants.MAX_SEQUENCE;
        if (sequence == 0) {
          currStamp = waitNextMill(lastStamp);
        }
      } else {
        sequence = 0L;
      }

      lastStamp = currStamp;

      return ((currStamp - SnowFlakeConstants.START_STAMP) << SnowFlakeConstants.TIMESTAMP_SHIFT)
          | (datacenterId << SnowFlakeConstants.DATACENTER_LEFT)
          | (machineId << SnowFlakeConstants.MACHINE_LEFT)
          | sequence;
    } finally {
      lock.unlock();
    }
  }

  /**
   * 等待下一毫秒的到来，用于处理同一毫秒内的序列号溢出问题.
   *
   * @param lastTimestamp 上次生成ID的时间戳
   * @return 下一毫秒的时间戳
   */
  private long waitNextMill(long lastTimestamp) {
    Instant now = Clock.systemUTC().instant();
    while (now.toEpochMilli() <= lastTimestamp) {
      now = Clock.systemUTC().instant();
    }
    return now.toEpochMilli();
  }

  /**
   * 获取当前时间的毫秒时间戳.
   *
   * @return 当前时间的毫秒时间戳
   */
  private long getNewStamp() {
    return System.currentTimeMillis();
  }

  /**
   * 获取数据中心ID的最大允许值.
   *
   * @return 最大数据中心ID
   */
  public static Long getMaxDataCenterNum() {
    return SnowFlakeConstants.MAX_DATACENTER_NUM;
  }

  /**
   * 获取机器ID的最大允许值.
   *
   * @return 最大机器ID
   */
  public static Long getMaxMachineNum() {
    return SnowFlakeConstants.MAX_MACHINE_NUM;
  }
}
