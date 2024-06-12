package org.zxx17.logistics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.WorkflowActionRequest;
import org.zxx17.logistics.controller.request.WorkflowCreateRequest;
import org.zxx17.logistics.controller.request.WorkflowDeleteRequest;
import org.zxx17.logistics.controller.request.WorkflowPageQueryRequest;
import org.zxx17.logistics.controller.request.WorkflowUpdateRequest;
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
      @RequestBody WorkflowCreateRequest request) {
    return workflowService.createWorkflow(request);
  }


  /**
   * 分页查询 FSM 状态机工作流程列表.
   */
  @PostMapping("/query")
  public Result<?> queryWorkflowList(
      @RequestBody WorkflowPageQueryRequest request) {
    return workflowService.queryWorkflowList(request);
  }

  /**
   * 更新 FSM 状态机工作流程.
   */
  @PostMapping("/update")
  public Result<?> updateWorkflow(
      @RequestBody WorkflowUpdateRequest request) {
    return workflowService.updateWorkflow(request);
  }

  /**
   * 删除 FSM 状态机工作流程.
   */
  @PostMapping("/delete")
  public Result<?> deleteWorkflow(
      @RequestBody WorkflowDeleteRequest request) {
    return workflowService.delete(request);
  }

  /**
   * FSM 状态机工作流程状态流转.
   */
  @PostMapping("/action")
  public Result<?> action(
      @RequestBody WorkflowActionRequest request) {
    return workflowService.action(request);
  }


}
