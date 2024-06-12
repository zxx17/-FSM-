package org.zxx17.logistics.manager;

import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.CreateWorkflowRequest;
import org.zxx17.logistics.controller.response.CommonResponse;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
public interface WorkflowManager {

  Result<CommonResponse> createWorkflow(CreateWorkflowRequest request);
}
