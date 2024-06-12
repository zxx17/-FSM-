package org.zxx17.logistics.service;

import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.BusinessAppCreateRequest;
import org.zxx17.logistics.controller.response.CommonResponse;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
public interface BusinessService {

  Result<CommonResponse> createBusinessApp(BusinessAppCreateRequest request);
}
