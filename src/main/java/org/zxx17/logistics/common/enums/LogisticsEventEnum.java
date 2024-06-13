package org.zxx17.logistics.common.enums;

import lombok.Getter;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
public enum LogisticsEventEnum {
  COLLECT("揽收"),
  PAY("支付"),
  SHIP("发货"),
  CANCEL("取消"),
  AUTO("自动执行"),
  DELIVERY("派送"),

  EXCEPTION("异常");

  private final String description;

  LogisticsEventEnum(String description) {
    this.description = description;
  }

}
