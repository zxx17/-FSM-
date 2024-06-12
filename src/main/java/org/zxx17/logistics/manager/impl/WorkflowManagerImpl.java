package org.zxx17.logistics.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.enums.LogisticsEventEnum;
import org.zxx17.logistics.common.enums.LogisticsStatusEnum;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.common.util.id.SnowFlake;
import org.zxx17.logistics.common.util.id.SnowFlakeFactory;
import org.zxx17.logistics.common.util.str.StringUtils;
import org.zxx17.logistics.container.WorkflowContainer;
import org.zxx17.logistics.container.WorkflowEventContainer;
import org.zxx17.logistics.container.WorkflowStatesContainer;
import org.zxx17.logistics.controller.request.WorkflowCreateRequest;
import org.zxx17.logistics.controller.request.WorkflowPageQueryRequest;
import org.zxx17.logistics.controller.request.WorkflowUpdateRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.controller.response.ErrorResponse;
import org.zxx17.logistics.controller.response.WorkFlowPageQueryResponse;
import org.zxx17.logistics.domain.dto.StatesDto;
import org.zxx17.logistics.domain.dto.WorkflowEventsDto;
import org.zxx17.logistics.domain.dto.WorkflowsDto;
import org.zxx17.logistics.domain.entity.WorkflowEvents;
import org.zxx17.logistics.domain.entity.WorkflowStates;
import org.zxx17.logistics.domain.entity.Workflows;
import org.zxx17.logistics.manager.WorkflowManager;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Slf4j
@Service
public class WorkflowManagerImpl implements WorkflowManager {

  @Resource
  private WorkflowContainer workflowContainer;

  @Resource
  private WorkflowEventContainer workflowEventContainer;
  @Resource
  private WorkflowStatesContainer workflowStatesContainer;
  @Resource
  private StateMachine<LogisticsStatusEnum, LogisticsStatusEnum> logisticsStateMachine;
  @Resource
  private StateMachinePersister<LogisticsStatusEnum, LogisticsStatusEnum, String>
      stateMachineMemoryPersisted;

  @Override
  public Result<CommonResponse> createWorkflow(WorkflowCreateRequest request) {
    SnowFlake snowFlake = SnowFlakeFactory.getSnowFlake();
    long workflowId = snowFlake.nextId();

    Long appId = request.getAppId();
    String workflowName = request.getName();
    String workflowDesc = request.getDesc();

    Workflows workflows = new Workflows();
    workflows.setId(workflowId);
    workflows.setApplicationId(appId);
    workflows.setName(workflowName);
    workflows.setDescription(workflowDesc);
    workflows.setCreator(StringUtils.randomName());
    workflows.setCreatedTime(new Date());
    workflowContainer.addWorkflows(workflows);

    List<StatesDto> workflowStates = request.getStates();
    List<WorkflowStates> workflowStatesList = new ArrayList<>();
    workflowStates.forEach(statesDTO -> {
      WorkflowStates workflowState = new WorkflowStates();
      workflowState.setId(snowFlake.nextId());
      workflowState.setWorkflowId(workflowId);
      workflowState.setStateCode(statesDTO.getCode());
      workflowState.setStateName(statesDTO.getName());
      workflowStatesList.add(workflowState);
    });
    workflowStatesContainer.addStates(workflowStatesList);

    List<WorkflowEventsDto> workflowEvents = request.getEvents();
    List<WorkflowEvents> workflowEventsList = new ArrayList<>();
    workflowEvents.forEach(workflowEventsDto -> {
      WorkflowEvents workflowEvent = new WorkflowEvents();
      workflowEvent.setId(snowFlake.nextId());
      workflowEvent.setWorkflowId(workflowId);
      workflowEvent.setEventName(workflowEventsDto.getName());
      workflowEvent.setFromState(workflowEventsDto.getFromState());
      workflowEvent.setToState(workflowEventsDto.getToState());
      workflowEvent.setRole(workflowEventsDto.getRole());
      workflowEventsList.add(workflowEvent);
    });
    workflowEventContainer.addWorkflowEvents(workflowEventsList);

    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setId(workflowId);
    return Result.response(commonResponse, "流程创建成功", ResultEnum.SUCCESS);
  }


  @Override
  public Result<WorkFlowPageQueryResponse> queryWorkflowList(WorkflowPageQueryRequest request) {
    Integer page = request.getPage();
    Integer size = request.getSize();

    ConcurrentHashMap<Long, Workflows> workflowsMap = workflowContainer.getWorkflowsMap();
    if (workflowsMap.isEmpty()) {
      WorkFlowPageQueryResponse response = new WorkFlowPageQueryResponse();
      response.setPage(page);
      response.setSize(size);
      return Result.response(response, "暂无流程", ResultEnum.SUCCESS);
    }

    // 获取所有工作流实例并转换为列表
    List<Workflows> workflowsList = new ArrayList<>(workflowsMap.values());

    // 按创建时间倒序排序
    workflowsList.sort(Comparator.comparing(Workflows::getCreatedTime).reversed());

    int total = workflowsList.size();

    // 确保分页逻辑正确处理边界情况
    int startIndex = Math.max(0, (page - 1) * size);
    int endIndex = Math.min(total, page * size);
    workflowsList = workflowsList.subList(startIndex, endIndex);

    WorkFlowPageQueryResponse response = new WorkFlowPageQueryResponse();
    response.setPage(page);
    response.setSize(size);
    response.setTotalPage((total + size - 1) / size); // 计算总页数
    response.setTotal(total);

    List<WorkflowsDto> workflowsDtoList = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    workflowsList.forEach(workflow -> {
      WorkflowsDto workflowsDto = new WorkflowsDto();
      workflowsDto.setName(workflow.getName());
      workflowsDto.setCreator(workflow.getCreator());
      workflowsDto.setId(workflow.getId());
      workflowsDto.setDesc(workflow.getDescription());
      workflowsDto.setCreatedTime(sdf.format(workflow.getCreatedTime()));
      workflowsDtoList.add(workflowsDto);
    });

    response.setContent(workflowsDtoList);

    return Result.response(response, "", ResultEnum.SUCCESS);
  }

  @Override
  public Result<?> deleteWorkflowById(Long workflowId) {
    // 工作流不存在不能删除
    Workflows workflows = workflowContainer.getWorkflowsMap().get(workflowId);
    if (workflows == null) {
      ErrorResponse response = new ErrorResponse();
      response.setReason(ResultEnum.PROCESS_ID_NOT_FOUND.getMessage());
      return Result.response(response, "流程删除失败", ResultEnum.PROCESS_ID_NOT_FOUND);
    }
    // 工作流进行中不能删除
    try {
      StateMachine<LogisticsStatusEnum, LogisticsStatusEnum> curInstanceStateMachine
          = stateMachineMemoryPersisted.restore(logisticsStateMachine, String.valueOf(workflowId));
      LogisticsStatusEnum curInstanceState = curInstanceStateMachine.getState().getId();
      if (!curInstanceState.equals(LogisticsStatusEnum.PENDING)) {
        ErrorResponse response = new ErrorResponse();
        response.setReason(ResultEnum.PROCESS_IN_PROGRESS.getMessage());
        return Result.response(response, "流程删除失败", ResultEnum.PROCESS_IN_PROGRESS);
      }
    } catch (Exception e) {
      log.error("FSM状态机工作流程状态查询失败:{}", e.getMessage());
      ErrorResponse response = new ErrorResponse();
      response.setReason(ResultEnum.SYSTEM_EXCEPTION.getMessage());
      return Result.response(response, "流程删除失败", ResultEnum.SYSTEM_EXCEPTION);
    }
    workflowContainer.deleteById(workflowId);
    workflowEventContainer.bachDeleteByWorkflowId(workflowId);
    workflowStatesContainer.bachDeleteByWorkflowId(workflowId);
    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setId(workflowId);
    return Result.response(commonResponse, "流程删除成功", ResultEnum.SUCCESS);
  }

  @Override
  public Result<?> updateWorkflow(WorkflowUpdateRequest request) {
    Long workflowId = request.getId();
    // 工作流不存在不能更新
    Workflows workflows = workflowContainer.getWorkflowsMap().get(workflowId);
    if (workflows == null) {
      ErrorResponse response = new ErrorResponse();
      response.setReason(ResultEnum.PROCESS_ID_NOT_FOUND.getMessage());
      return Result.response(response, "流程更新失败", ResultEnum.PROCESS_ID_NOT_FOUND);
    }
    // 工作流进行中不能更新
    try {
      StateMachine<LogisticsStatusEnum, LogisticsStatusEnum> curInstanceStateMachine
          = stateMachineMemoryPersisted.restore(logisticsStateMachine,
          String.valueOf(workflowId));
      LogisticsStatusEnum curInstanceState = curInstanceStateMachine.getState().getId();
      if (!curInstanceState.equals(LogisticsStatusEnum.PENDING)) {
        ErrorResponse response = new ErrorResponse();
        response.setReason(ResultEnum.PROCESS_IN_PROGRESS.getMessage());
        return Result.response(response, "流程更新失败", ResultEnum.PROCESS_IN_PROGRESS);
      }
    } catch (Exception e) {
      log.error("FSM状态机工作流程状态查询失败:{}", e.getMessage());
      ErrorResponse response = new ErrorResponse();
      response.setReason(ResultEnum.SYSTEM_EXCEPTION.getMessage());
      return Result.response(response, "流程更新失败", ResultEnum.SYSTEM_EXCEPTION);
    }

    List<StatesDto> states = request.getStates();
    List<WorkflowEventsDto> events = request.getEvents();

    workflowContainer.updateById(workflowId, request.getName(), request.getDesc());
    workflowEventContainer.batchUpdateByWorkflowId(workflowId, events);
    workflowStatesContainer.bachUpdateByWorkflowId(workflowId, states);

    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setId(workflowId);
    return Result.response(commonResponse, "流程更新成功", ResultEnum.SUCCESS);
  }

  @Override
  public synchronized int sendEvent(Long workflowId, String action, String role) {
    // TODO 校验workflowId存不存在

    log.info("开始执行状态机，事件{}，角色{}", action, role);
    boolean result = false;

    try {
      //启动状态机
      logisticsStateMachine.start();
      //尝试恢复状态机状态
      StateMachine<LogisticsStatusEnum, LogisticsStatusEnum> curInstanceStateMachine
          = stateMachineMemoryPersisted.restore(logisticsStateMachine, String.valueOf(workflowId));
      LogisticsStatusEnum curInstanceState = curInstanceStateMachine.getState().getId();
      // TODO 角色权限校验 TODO 非法状态轮转需要直接驳回

      Message message = MessageBuilder.withPayload(LogisticsEventEnum.valueOf(action))
          .setHeader("workflowId", workflowId).build();
      log.info("发送事件，消息>>>>{}", message);
      result = logisticsStateMachine.sendEvent(message);
      if (!result) {
        // 状态机执行失败
        return 0;
      }
      //获取到监听的结果信息
      Integer o = (Integer) logisticsStateMachine.getExtendedState().getVariables()
          .get(action + workflowId);
      log.info("获取到监听的结果信息>>>>{}", o);
      //操作完成之后,删除本次对应的key信息
      logisticsStateMachine.getExtendedState().getVariables().remove(action + workflowId);
      //如果事务执行成功，则持久化状态机
      if (Objects.equals(1, Integer.valueOf(o))) {
        //持久化状态机状态 nb
        stateMachineMemoryPersisted.persist(logisticsStateMachine, String.valueOf(workflowId));
      } else {
        // 执行异常
        return -1;
      }

    } catch (Exception e) {
      log.error("FSM状态机工作流程状态流转失败:{}", e.getMessage());
    } finally {
      logisticsStateMachine.stop();
    }
    return 1;
  }

}
