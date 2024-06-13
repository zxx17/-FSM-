package org.zxx17.logistics.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.enums.LogisticsBizAppStatesEnum;
import org.zxx17.logistics.common.enums.LogisticsStatusEnum;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.enums.RoleEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.BusinessAppCreateRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.domain.dto.RoleAuthDto;
import org.zxx17.logistics.domain.dto.RolesDto;
import org.zxx17.logistics.domain.dto.StatesDto;
import org.zxx17.logistics.manager.BusinessManger;
import org.zxx17.logistics.service.BusinessService;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

  private final BusinessManger businessManger;

  @Override
  public Result<CommonResponse> createBusinessApp(BusinessAppCreateRequest request) {
    // 1.校验状态
    List<StatesDto> states = request.getStates();
    String beginState = request.getBeginState();
    String endState = request.getEndState();
    // 1.1校验状态数量
    if (states.size() < 3) {
      log.error("创建业务应用失败，状态总数不能少于 3 个");
      return Result.response("创建业务应用失败", ResultEnum.TOO_FEW_STATES);
    }
    // 1.2开始和结束状态合法性
    boolean illegalState = checkErrorStartStateToEndState(beginState, endState, states);
    if (illegalState) {
      log.error("创建业务应用失败，开始状态或结束状态不合理");
      return Result.response("创建业务应用失败", ResultEnum.INVALID_START_OR_END_STATE);
    }
    // 2.校验角色 主要是校验角色开始状态和结束状态是否非法，一致非法，反向非法
    boolean illegalRoleState =
        checkRolesErrorStartStateToEndState(request.getRoles(), states.size());
    if (illegalRoleState) {
      log.error("创建业务应用失败，角色开始状态或结束状态不合理");
      return Result.response("创建业务应用失败", ResultEnum.UNREASONABLE_USER_ROLE_PERMISSION);
    }

    // 创建业务应用
    return businessManger.handleCreateBusinessApp(request);
  }

  private boolean checkRolesErrorStartStateToEndState(List<RolesDto> rolesDtos, int stateSize) {
    boolean isCheckEnum = false;
    String fromState = rolesDtos.get(0).getAuth().get(0).getFromState();
    for (LogisticsStatusEnum logisticsStatusEnum : LogisticsStatusEnum.values()) {
      if (logisticsStatusEnum.name().equals(fromState)) {
        isCheckEnum = true;
        break;
      }
    }
    // 封装map
    Map<String, List<RoleAuthDto>> roleAndAuths = new HashMap<>();
    for (RolesDto rolesDto : rolesDtos) {
      String roleName = rolesDto.getRole();
      List<RoleAuthDto> roleAuth = rolesDto.getAuth();
      roleAndAuths.put(roleName, roleAuth);
    }
    if (!isCheckEnum) {
      // 1.对于S类的状态，
      // 对每个roleName的roleAuth进行checkErrorStartStateToEndState，返回true停止
      for (Map.Entry<String, List<RoleAuthDto>> entry : roleAndAuths.entrySet()) {
        List<RoleAuthDto> roleAuth = entry.getValue();
        for (RoleAuthDto roleAuthDto : roleAuth) {
          String fromStateStr = roleAuthDto.getFromState();
          String toStateStr = roleAuthDto.getToState();
          int startFlag = Integer.parseInt(
              fromStateStr.substring(fromStateStr.lastIndexOf("_") + 1));
          int endStateFlag = Integer.parseInt(
              toStateStr.substring(toStateStr.lastIndexOf("_") + 1));
          if (
              startFlag >= endStateFlag
          ) {
            if (stateSize == 3 && fromStateStr.equals(LogisticsBizAppStatesEnum.BEGIN.getCode())) {
              // 说明中转只存在一轮
              return true;
            }
            if (stateSize > 3 && !fromStateStr.equals(LogisticsBizAppStatesEnum.END.getCode())) {
              return false;
            }
            log.error("role====>{}开始状态和结束状态不非法checking！！！！！！！！！！！！！！{}---->{}",
                entry.getKey(), fromStateStr, toStateStr);
            return true;
          }
        }
      }
    } else {
      // 2.对于枚举类的状态
      for (Map.Entry<String, List<RoleAuthDto>> entry : roleAndAuths.entrySet()) {
        List<RoleAuthDto> roleAuth = entry.getValue();
        for (RoleAuthDto roleAuthDto : roleAuth) {
          String fromStateStr = roleAuthDto.getFromState();
          String toStateStr = roleAuthDto.getToState();
          boolean rightStateTrans = isValidTransition(
              LogisticsStatusEnum.valueOf(fromStateStr),
              LogisticsStatusEnum.valueOf(toStateStr),
              RoleEnum.valueOf(entry.getKey())
          );
          if (!rightStateTrans) {
            log.error("role====>{}开始状态和结束状态不非法checking2222！！！！！！！！！！！！！！{}---->{}",
                entry.getKey(), fromStateStr, toStateStr);
            return true;
          }
        }
      }

    }

    return false;
  }


  /**
   * 判断角色开始状态和结束状态是否非法.
   */
  public static boolean isValidTransition(LogisticsStatusEnum fromState,
      LogisticsStatusEnum toState,
      RoleEnum role) {
    switch (role) {
      case POSTMAN:
        return
            (fromState == LogisticsStatusEnum.PENDING && toState == LogisticsStatusEnum.COLLECTED)
                || (fromState == LogisticsStatusEnum.TRANSITING
                && toState == LogisticsStatusEnum.DELIVERY)
                || (fromState == LogisticsStatusEnum.DELIVERY
                && toState == LogisticsStatusEnum.EXCEPTION);

      case SENDER:
        return (fromState == LogisticsStatusEnum.PAYING && toState == LogisticsStatusEnum.PAID)
            || (fromState == LogisticsStatusEnum.PENDING
            && toState == LogisticsStatusEnum.CANCELLED)
            || (fromState == LogisticsStatusEnum.PAYING
            && toState == LogisticsStatusEnum.CANCELLED);

      case TRANSITER:
      case DRIVER:
        return (fromState == LogisticsStatusEnum.PAID && toState == LogisticsStatusEnum.TRANSITING);

      case RECIPIENT:
        return
            (fromState == LogisticsStatusEnum.DELIVERY && toState == LogisticsStatusEnum.DELIVERED)
                || (fromState == LogisticsStatusEnum.DELIVERY
                && toState == LogisticsStatusEnum.REFUSED);

      case AUTO:
        return
            (fromState == LogisticsStatusEnum.DELIVERED && toState == LogisticsStatusEnum.COMPLETED)
                || (fromState == LogisticsStatusEnum.CANCELLED
                && toState == LogisticsStatusEnum.COMPLETED)
                || (fromState == LogisticsStatusEnum.REFUSED
                && toState == LogisticsStatusEnum.COMPLETED)
                || (fromState == LogisticsStatusEnum.EXCEPTION
                && toState == LogisticsStatusEnum.COMPLETED)
                || (fromState == LogisticsStatusEnum.COLLECTED
                && toState == LogisticsStatusEnum.PAYING);

      default:
        return false;
    }
  }


  private boolean checkErrorStartStateToEndState(String beginState, String endState,
      List<StatesDto> states) {
    // 循环比较看是不是    LogisticsStatusEnum
    for (LogisticsStatusEnum statusEnum : LogisticsStatusEnum.values()) {
      if (statusEnum.name().equals(beginState)) {
        // check1
        return !validBeginAndEndTransition(LogisticsStatusEnum.valueOf(beginState),
            LogisticsStatusEnum.valueOf(endState));
      }
    }
    // check2（待改成enum）
    String maxState = states.get(states.size() - 1).getCode();
    int startFlag = Integer.parseInt(beginState.substring(beginState.lastIndexOf("_") + 1));
    int endStateFlag = Integer.parseInt(endState.substring(endState.lastIndexOf("_") + 1));
    int maxStateFlag = Integer.parseInt(maxState.substring(maxState.lastIndexOf("_") + 1));
    return startFlag >= endStateFlag || endStateFlag > maxStateFlag;
  }


  private boolean validBeginAndEndTransition(LogisticsStatusEnum fromState,
      LogisticsStatusEnum toState) {
    // 待补充
    if (fromState.equals(LogisticsStatusEnum.PENDING)) {
      return toState.equals(LogisticsStatusEnum.COMPLETED);
    }
    return true;
  }


}



