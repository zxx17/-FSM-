package org.zxx17.logistics.constant;

/**
 * 雪花算法常量.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
public final class SnowFlakeConstants {

  /**
   * 属性未找到或无效时的默认返回值.
   */
  public static final long DEFAULT_VALUE = 0L;

  /**
   * 数据中心ID对应的配置键名.
   */
  public static final String DATA_CENTER_ID = "data.center.id";

  /**
   * 机器ID对应的配置键名.
   */
  public static final String MACHINE_ID = "machine.id";

  /**
   * 默认数据中心id.
   */
  public static final long DEFAULT_DATACENTER_ID = 1;
  /**
   * 默认的机器id.
   */
  public static final long DEFAULT_MACHINE_ID = 1;

  /**
   * 默认的雪花算法句柄.
   */
  public static final String DEFAULT_SNOW_FLAKE = "snow_flake";

  /**
   * 开始时间戳，用于计算距离某个固定时间的偏移量.
   */
  public static final long START_STAMP = 1649735805910L;

  /**
   * 序列号占用的位数.
   */
  public static final long SEQUENCE_BIT = 12;
  /**
   * 机器标识占用的位数.
   */
  public static final long MACHINE_BIT = 5;
  /**
   * 数据中心占用的位数.
   */
  public static final long DATACENTER_BIT = 5;

  /**
   * 数据中心ID的最大值.
   */
  public static final long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
  /**
   * 机器ID的最大值.
   */
  public static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
  /**
   * 序列号的最大值.
   */
  public static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

  /**
   * 机器ID左移的位数.
   */
  public static final long MACHINE_LEFT = SnowFlakeConstants.SEQUENCE_BIT;
  /**
   * 数据中心ID左移的位数.
   */
  public static final long DATACENTER_LEFT =
      SnowFlakeConstants.SEQUENCE_BIT + SnowFlakeConstants.MACHINE_BIT;
  /**
   * 时间戳左移的位数.
   */
  public static final long TIMESTAMP_SHIFT = DATACENTER_LEFT + SnowFlakeConstants.DATACENTER_BIT;

}
