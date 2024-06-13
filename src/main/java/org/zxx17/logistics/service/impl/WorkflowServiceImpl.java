package org.zxx17.logistics.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.enums.LogisticsStatusEnum;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.WorkflowActionRequest;
import org.zxx17.logistics.controller.request.WorkflowCreateRequest;
import org.zxx17.logistics.controller.request.WorkflowDeleteRequest;
import org.zxx17.logistics.controller.request.WorkflowPageQueryRequest;
import org.zxx17.logistics.controller.request.WorkflowUpdateRequest;
import org.zxx17.logistics.controller.response.ErrorResponse;
import org.zxx17.logistics.domain.dto.StatesDto;
import org.zxx17.logistics.domain.dto.WorkflowEventsDto;
import org.zxx17.logistics.manager.WorkflowManager;
import org.zxx17.logistics.service.WorkflowService;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowServiceImpl implements WorkflowService {

  private final WorkflowManager workflowManager;

  @Override
  public Result<?> createWorkflow(WorkflowCreateRequest request) {
    // 0.校验流程名称 TODO 应该放到统一的地方进行入参校验
    String name = request.getName();
    if (name == null || name.equals("")) {
      ErrorResponse errorResponse = new ErrorResponse();
      errorResponse.setReason(ResultEnum.INPUT_PARAMETER_ERROR.getMessage());
      return Result.response(errorResponse,
          "创建失败",
          ResultEnum.INPUT_PARAMETER_ERROR
      );
    }
    List<WorkflowEventsDto> events = request.getEvents();
    // 1.出现fromState-->toState不合理时直接返回 分两类
    int check = 0;
    for (LogisticsStatusEnum logisticsStatusEnum : LogisticsStatusEnum.values()) {
      if (logisticsStatusEnum.name().equals(events.get(0).getFromState())) {
        check = 1;
        break;
      }
    }
    // 1.1
    if (check == 1) {
      for (WorkflowEventsDto event : events) {
        LogisticsStatusEnum fromState = LogisticsStatusEnum.valueOf(event.getFromState());
        LogisticsStatusEnum toState = LogisticsStatusEnum.valueOf(event.getToState());
        if (!isValidTransition(fromState, toState)) {
          ErrorResponse response = new ErrorResponse();
          response.setReason(ResultEnum.ILLEGAL_STATE_TRANSITION.getMessage());
          log.warn("非法的状态轮转: " + fromState + " --> " + toState);
          return Result.response(response,
              "创建失败",
              ResultEnum.ILLEGAL_STATE_TRANSITION
          );
        }
      }
    }
    // 1.2 并解决了循环依赖问题
    if (check == 0) {
      for (WorkflowEventsDto event : events) {
        String fromStateStr = event.getFromState();
        String toStateStr = event.getToState();
        int startFlag = Integer.parseInt(fromStateStr.substring(fromStateStr.lastIndexOf("_") + 1));
        int endStateFlag = Integer.parseInt(toStateStr.substring(toStateStr.lastIndexOf("_") + 1));
        if (startFlag >= endStateFlag) {
          ErrorResponse response = new ErrorResponse();
          response.setReason(ResultEnum.ILLEGAL_STATE_TRANSITION.getMessage());
          log.warn("非法的状态轮转: " + startFlag + " --> " + endStateFlag);
          return Result.response(response,
              "创建失败",
              ResultEnum.ILLEGAL_STATE_TRANSITION
          );
        }
      }
    }

    // 2.事件中的状态扭转应该定义在全部状态中
    boolean illegalTransitionState = checkEventsState(events, request.getStates());
    if (illegalTransitionState) {
      ErrorResponse response = new ErrorResponse();
      // 开始状态或者结束状态不在状态列表中
      log.error("======开始状态或者结束状态不在状态列表中======");
      response.setReason(ResultEnum.START_OR_END_STATE_NOT_IN_LIST.getMessage());
      return Result.response(response,
          "创建失败",
          ResultEnum.START_OR_END_STATE_NOT_IN_LIST
      );
    }

    return workflowManager.createWorkflow(request);
  }

  private boolean checkEventsState(List<WorkflowEventsDto> events, List<StatesDto> states) {
    Map<String, String> statesMap = new HashMap<>();
    states.forEach(state -> {
      String code = state.getCode();
      String name = state.getName();
      statesMap.put(code, name);
    });
    for (WorkflowEventsDto event : events) {
      String fromState = event.getFromState();
      String toState = event.getToState();
      if (!statesMap.containsKey(fromState) || !statesMap.containsKey(toState)) {
        return true;
      }
    }
    return false;

  }


  @Override
  public Result<?> queryWorkflowList(WorkflowPageQueryRequest request) {
    return workflowManager.queryWorkflowList(request);
  }

  @Override
  public Result<?> action(WorkflowActionRequest request) {
    Long workflowId = request.getId();
    String action = request.getAction();
    String role = request.getRole();
    log.info(">>>>>FSM状态机工作流程状态流转: workflowId={}, action={}, role={}",
        workflowId, action,
        role);
    int flag = workflowManager.sendEvent(workflowId, action, role);
    // 1. 用户角色权限不合理
    if (flag == 2) {
      ErrorResponse response = new ErrorResponse();
      response.setReason(ResultEnum.UNREASONABLE_USER_ROLE_PERMISSION.getMessage());
      return Result.response(response, "操作失败", ResultEnum.UNREASONABLE_USER_ROLE_PERMISSION);
    }
    //2. 系统异常
    if (flag == 0) {
      ErrorResponse response = new ErrorResponse();
      response.setReason(ResultEnum.SYSTEM_EXCEPTION.getMessage());
      return Result.response(response, "操作失败", ResultEnum.SYSTEM_EXCEPTION);
    }
    return Result.response(null, "操作成功", ResultEnum.SUCCESS);

  }

  @Override
  public Result<?> delete(WorkflowDeleteRequest request) {
    Long workflowId = request.getId();
    return workflowManager.deleteWorkflowById(workflowId);
  }

  @Override
  public Result<?> updateWorkflow(WorkflowUpdateRequest request) {
    List<WorkflowEventsDto> events = request.getEvents();
    // TODO出现fromState-->toState不合理时直接返回
    for (WorkflowEventsDto event : events) {
      LogisticsStatusEnum fromState = LogisticsStatusEnum.valueOf(event.getFromState());
      LogisticsStatusEnum toState = LogisticsStatusEnum.valueOf(event.getToState());

      if (!isValidTransition(fromState, toState)) {
        ErrorResponse response = new ErrorResponse();
        response.setReason(ResultEnum.ILLEGAL_STATE_TRANSITION.getMessage());
        log.warn("Invalid state transition: " + fromState + " --> " + toState);
        return Result.response(response,
            "更新失败",
            ResultEnum.ILLEGAL_STATE_TRANSITION
        );
      }
    }

    return workflowManager.updateWorkflow(request);
  }

  /**
   * 检查物流状态转换是否有效. 确保状态转换遵循定义的物流工作流程.
   *
   * @param fromState 当前物流状态
   * @param toState   目标物流状态
   * @return 如果转换有效，则返回true；否则返回false
   */
  private boolean isValidTransition(LogisticsStatusEnum fromState, LogisticsStatusEnum toState) {
    switch (fromState) {
      case PENDING:       // 初始状态，仅在准备就绪后可转为已收集。
        return toState == LogisticsStatusEnum.COLLECTED;

      case COLLECTED:     // 收集后，可继续进行支付或若已当场付款则直接完成。
        return toState == LogisticsStatusEnum.PAYING || toState == LogisticsStatusEnum.COMPLETED;

      case PAYING:        // 一旦开始支付过程，应以已付款或已取消结束。
        return toState == LogisticsStatusEnum.PAID || toState == LogisticsStatusEnum.CANCELLED;

      case PAID:          // 已付款的物品进入运输阶段。
        return toState == LogisticsStatusEnum.TRANSITING;

      case TRANSITING:    // 运输中的物品随后进行配送。
        return toState == LogisticsStatusEnum.DELIVERY;

      case DELIVERY:      // 配送结果为成功送达、拒收或出现异常。
        return toState == LogisticsStatusEnum.DELIVERED
            || toState == LogisticsStatusEnum.REFUSED
            || toState == LogisticsStatusEnum.EXCEPTION;

      case DELIVERED:     // 成功送达的物品视为已完成。
      case REFUSED:       // 拒收的物品同样移至完成状态以记录。
      case EXCEPTION:     // 任何异常情况通过标记为已完成解决。
      case CANCELLED:     // 已取消的订单以完成状态关闭。
        return toState == LogisticsStatusEnum.COMPLETED;

      case COMPLETED:     // 完成是最终状态，不允许进一步转换。
        // 'COMPLETED' 不应转换为任何其他状态
        return false;

      default:           // 处理任何未预期的状态。
        // 如有需要，此处可记录或处理未识别的状态。
        return false;
    }
  }


}
