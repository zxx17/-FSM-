package org.zxx17.logistics.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.CreateWorkflowRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.service.WorkflowService;

/**
 * 工作流程接口.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
public class WorkflowController {

  private final WorkflowService workflowService;

  /**
   * 创建 FSM 状态机工作流程.
   */
  @PostMapping("/create")
  public Result<?> createWorkflow(
      @RequestBody CreateWorkflowRequest request) {
    return workflowService.createWorkflow(request);
  }


  /**
   * 分页查询 FSM 状态机工作流程列表.
   */


  /**
   * 更新 FSM 状态机工作流程.
   */


  /**
   * 删除 FSM 状态机工作流程.
   */


  /**
   * FSM 状态机工作流程状态流转.
   */
}
