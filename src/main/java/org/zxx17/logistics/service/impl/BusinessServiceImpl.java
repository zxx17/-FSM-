package org.zxx17.logistics.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.BusinessAppCreateRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
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
    List<StatesDto> states = request.getStates();
    // TODO 这里分两类
    String beginState = request.getBeginState();
    String endState = request.getEndState();
    // 校验状态数量
    if (states.size() < 3) {
      return Result.response("创建业务应用失败", ResultEnum.TOO_FEW_STATES);
    }

    // 创建业务应用
    return businessManger.handleCreateBusinessApp(request);
  }



}



