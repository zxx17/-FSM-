package org.zxx17.logistics.manager;

import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.CreateBusinessAppRequest;
import org.zxx17.logistics.controller.response.CommonResponse;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @since 2024-06-11
 */
public interface BusinessManger {

  Result<CommonResponse> handleCreateBusinessApp(CreateBusinessAppRequest request);
}
