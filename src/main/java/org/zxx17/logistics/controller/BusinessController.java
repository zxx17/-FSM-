package org.zxx17.logistics.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.BusinessAppCreateRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.service.BusinessService;

/**
 * 业务应用接口.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class BusinessController {

  private final BusinessService businessService;

  /**
   * 创建业务应用.
   */
  @PostMapping("/create")
  public Result<CommonResponse> createBusinessApp(
      @RequestBody BusinessAppCreateRequest request) {
    return businessService.createBusinessApp(request);
  }

}
