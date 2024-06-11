package org.zxx17.logistics.common.enums;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
public enum LogisticsStatusEnum {
  PENDING("待揽收"),
  COLLECTED("已揽收"),
  PAYING("待支付"),
  PAID("已支付"),
  TRANSITING("运输中"),
  DELIVERY("派送中"),
  DELIVERID("已签收"),
  REFUSED("已拒收"),
  EXCEPTION("异常件"),
  CANCELLED("已取消"),
  COMPLETED("已完成");

  private String description;

  LogisticsStatusEnum(String description) {
    this.description = description;
  }


}
