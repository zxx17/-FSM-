package org.zxx17.logistics.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.CreateBusinessAppRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
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
  public Result<CommonResponse> createBusinessApp(CreateBusinessAppRequest request) {
    // TODO 错误的校验
    return businessManger.handleCreateBusinessApp(request);
  }


}
