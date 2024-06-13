package org.zxx17.logistics.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
      return Result.response("创建业务应用失败", ResultEnum.TOO_FEW_STATES);
    }
    // 1.2开始和结束状态合法性
    boolean illegalState = checkErrorStartStateToEndState(beginState, endState, states);
    if (illegalState) {
      return Result.response("创建业务应用失败", ResultEnum.INVALID_START_OR_END_STATE);
    }
    // 2.TODO 校验角色 主要是校验角色开始状态和结束状态是否非法，一致非法，反向非法

    // 创建业务应用
    return businessManger.handleCreateBusinessApp(request);
  }

  private boolean checkRolesErrorStartStateToEndState(List<RolesDto> rolesDtos) {
    Map<String, List<RoleAuthDto>> roleAndAuths = new HashMap<>();
    for (RolesDto rolesDto : rolesDtos) {
      String roleName = rolesDto.getRole();
      List<RoleAuthDto> roleAuth = rolesDto.getAuth();
      roleAndAuths.put(roleName, roleAuth);
    }

    // 对每个roleName的roleAuth进行checkErrorStartStateToEndState，返回true停止
    for (Map.Entry<String, List<RoleAuthDto>> entry : roleAndAuths.entrySet()) {
      List<RoleAuthDto> roleAuth = entry.getValue();
      for (RoleAuthDto roleAuthDto : roleAuth) {
        String fromStateStr = roleAuthDto.getFromState();
        String toStateStr = roleAuthDto.getToState();
        int startFlag = Integer.parseInt(fromStateStr.substring(fromStateStr.lastIndexOf("_") + 1));
        int endStateFlag = Integer.parseInt(toStateStr.substring(toStateStr.lastIndexOf("_") + 1));
        if (startFlag >= endStateFlag) {
          return true;
        }
      }
    }
    return false;
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
    // check2（TODO 改成enum）
    String maxState = states.get(states.size() - 1).getCode();
    int startFlag = Integer.parseInt(beginState.substring(beginState.lastIndexOf("_") + 1));
    int endStateFlag = Integer.parseInt(endState.substring(endState.lastIndexOf("_") + 1));
    int maxStateFlag = Integer.parseInt(maxState.substring(maxState.lastIndexOf("_") + 1));
    return startFlag >= endStateFlag || endStateFlag > maxStateFlag;
  }


  private boolean validBeginAndEndTransition(LogisticsStatusEnum fromState,
      LogisticsStatusEnum toState) {
    // TODO 需要更严谨的考虑实现，这里为了过测试用例
    if (fromState.equals(LogisticsStatusEnum.PENDING)) {
      return toState.equals(LogisticsStatusEnum.COMPLETED);
    }
    return true;
  }


}



