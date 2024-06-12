package org.zxx17.logistics.manager;

import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.WorkflowCreateRequest;
import org.zxx17.logistics.controller.request.WorkflowPageQueryRequest;
import org.zxx17.logistics.controller.request.WorkflowUpdateRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.controller.response.WorkFlowPageQueryResponse;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
public interface WorkflowManager {

  Result<CommonResponse> createWorkflow(WorkflowCreateRequest request);

  Result<WorkFlowPageQueryResponse> queryWorkflowList(WorkflowPageQueryRequest request);

  int sendEvent(Long workflowId, String action, String role);

  Result<?> deleteWorkflowById(Long workflowId);

  Result<?> updateWorkflow(WorkflowUpdateRequest request);
}
