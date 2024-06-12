package org.zxx17.logistics.manager.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.common.util.id.SnowFlake;
import org.zxx17.logistics.common.util.id.SnowFlakeFactory;
import org.zxx17.logistics.container.WorkflowContainer;
import org.zxx17.logistics.container.WorkflowEventContainer;
import org.zxx17.logistics.container.WorkflowStatesContainer;
import org.zxx17.logistics.controller.request.CreateWorkflowRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.domain.dto.StatesDTO;
import org.zxx17.logistics.domain.dto.WorkflowEventsDto;
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
@RequiredArgsConstructor
public class WorkflowManagerImpl implements WorkflowManager {

  private final WorkflowContainer workflowContainer;

  private final WorkflowEventContainer workflowEventContainer;

  private final WorkflowStatesContainer workflowStatesContainer;

  @Override
  public Result<CommonResponse> createWorkflow(CreateWorkflowRequest request) {
    SnowFlake snowFlake = SnowFlakeFactory.getSnowFlake();
    long workflowId = snowFlake.nextId();

    Long appId = request.getAppId();
    String workflowName = request.getName();
    String workflowDesc = request.getDesc();
    List<StatesDTO> workflowStates = request.getStates();
    List<WorkflowEventsDto> workflowEvents = request.getEvents();

    Workflows workflows = new Workflows();
    workflows.setId(workflowId);
    workflows.setApplicationId(appId);
    workflows.setName(workflowName);
    workflows.setDescription(workflowDesc);
    workflowContainer.addWorkflows(workflows);

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


}
