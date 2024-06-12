package org.zxx17.logistics.common.enums;

import lombok.Getter;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 **/
@Getter
public enum LogisticsBizAppStatesEnum {

  /**
   * 开始.
   */
  BEGIN("S_0", "开始"),

  /**
   * 中转.
   */
  TRANSFER("S_1", "中转"),

  /**
   * 结束.
   */
  END("S_2", "结束"),

  ;

  private final String code;
  private final String name;

  LogisticsBizAppStatesEnum(String code, String name) {
    this.code = code;
    this.name = name;
  }


}
