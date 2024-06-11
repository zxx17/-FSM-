package org.zxx17.logistics.common.util.id;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.zxx17.logistics.constant.SnowFlakeConstants;


/**
 * SnowFlakeLoader 类用于加载 SnowFlake 算法所需的配置信息.
 * 包括数据中心ID和机器ID，这些信息存储在外部配置文件中.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Slf4j
public class SnowFlakeLoader {
  
  /**
   * 存储配置信息的Properties对象，使用volatile保证多线程下的可见性.
   */
  private static volatile Properties instance;

  static {
    // 使用try-with-resources自动关闭输入流，加载snowflake配置文件.
    try (InputStream in = SnowFlakeLoader.class.getClassLoader()
        .getResourceAsStream("snowflake/snowflake.properties")) {
      if (in != null) {
        instance = new Properties();
        instance.load(in);
      } else {
        log.error("找不到Snowflake配置文件，请确保snowflake.properties存在于类路径中.");
      }
    } catch (IOException e) {
      log.error("加载Snowflake配置文件时发生错误.", e);
    }
  }

  /**
   * 双重检查锁定模式确保Properties实例被正确初始化.
   *
   * @return 已初始化的Properties实例
   * @throws IllegalStateException 如果实例未被正确初始化
   */
  private static Properties getInstance() {
    if (instance == null) {
      synchronized (SnowFlakeLoader.class) {
        if (instance == null) {
          throw new IllegalStateException("SnowFlakeLoader 未被正确初始化；配置信息缺失.");
        }
      }
    }
    return instance;
  }

  /**
   * 获取数据中心ID.
   *
   * @return 数据中心ID，如果未找到或无效则返回0
   */
  public static Long getDataCenterId() {
    return getLongValue(SnowFlakeConstants.DATA_CENTER_ID);
  }

  /**
   * 获取机器ID.
   *
   * @return 机器ID，如果未找到或无效则返回0
   */
  public static Long getMachineId() {
    return getLongValue(SnowFlakeConstants.MACHINE_ID);
  }

  /**
   * 根据键名从配置中获取长整型值.
   *
   * @param key 配置项的键名
   * @return 解析得到的长整型值，如果解析失败或键不存在则返回DEFAULT_VALUE
   */
  private static Long getLongValue(String key) {
    Properties props = getInstance();
    String value = props.getProperty(key);
    return (value == null || value.trim().isEmpty()) 
        ? SnowFlakeConstants.DEFAULT_VALUE : Long.parseLong(value.trim());
  }
}

