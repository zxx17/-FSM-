package org.zxx17.logistics.service;

import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.CreateBusinessAppRequest;

/**
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
public interface BusinessService {

  Result<Long> createBusinessApp(CreateBusinessAppRequest request);
}
