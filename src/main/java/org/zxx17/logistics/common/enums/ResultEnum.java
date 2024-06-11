package org.zxx17.logistics.common.enums;

import lombok.Getter;

/**
 * 返回码定义.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
@Getter
public enum ResultEnum {

  SUCCESS(20000, "操作成功"),
  INVALID_START_OR_END_STATE(50010, "开始状态或结束状态不合理"),
  TOO_FEW_STATES(50011, "状态总数不能少于 3 个"),
  START_OR_END_STATE_NOT_IN_LIST(50012, "开始状态或结束状态不在状态列表中"),
  UNREASONABLE_USER_ROLE_PERMISSION(50013, "用户角色权限不合理"),
  UNREASONABLE_STATE_TRANSITION(50020, "状态转换不合理"),
  MISSING_REQUIRED_STATE(50021, "缺少必要状态"),
  ILLEGAL_STATE_NAME(50022, "非法的状态名"),
  CYCLIC_DEPENDENCY(50023, "存在循环依赖的状态流转关系"),
  STATE_RELATIONSHIP_WITHOUT_TRIGGER(50024, "存在没有触发条件的状态关系"),
  APP_ID_NOT_FOUND(50031, "appId 不存在"),
  PROCESS_ID_NOT_FOUND(50032, "流程 ID 不存在"),
  PROCESS_IN_PROGRESS(50033, "流程正在进行中"),
  ILLEGAL_STATE_TRANSITION(50040, "非法的状态流转"),
  INPUT_PARAMETER_ERROR(50050, "输入参数错误"),
  SYSTEM_EXCEPTION(50502, "系统异常，请稍后重试");
  private final int code;
  private final String message;

  ResultEnum(int code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * 根据状态码查找枚举.
   *
   * @param code 状态码
   * @return ResultEnum
   */
  public static ResultEnum findByCode(int code) {
    for (ResultEnum resultEnum : ResultEnum.values()) {
      if (resultEnum.getCode() == code) {
        return resultEnum;
      }
    }
    return null;
  }

}
