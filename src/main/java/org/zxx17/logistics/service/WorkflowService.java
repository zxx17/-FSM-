package org.zxx17.logistics.service;

import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.WorkflowActionRequest;
import org.zxx17.logistics.controller.request.WorkflowCreateRequest;
import org.zxx17.logistics.controller.request.WorkflowDeleteRequest;
import org.zxx17.logistics.controller.request.WorkflowPageQueryRequest;
import org.zxx17.logistics.controller.request.WorkflowUpdateRequest;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
public interface WorkflowService {

  Result<?> createWorkflow(WorkflowCreateRequest request);

  Result<?> queryWorkflowList(WorkflowPageQueryRequest request);

  Result<?> action(WorkflowActionRequest request);

  Result<?> delete(WorkflowDeleteRequest request);

  Result<?> updateWorkflow(WorkflowUpdateRequest request);
}
