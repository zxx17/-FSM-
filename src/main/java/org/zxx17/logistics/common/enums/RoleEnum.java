package org.zxx17.logistics.common.enums;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
public enum RoleEnum {
  POSTMAN("小哥"),
  SENDER("寄件人"),
  TRANSITER("转运员"),
  DRIVER("司机"),
  RECIPIENT("收件人");

  private String description;

  RoleEnum(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

}
