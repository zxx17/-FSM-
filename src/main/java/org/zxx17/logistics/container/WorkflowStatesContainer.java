package org.zxx17.logistics.container;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.domain.dto.StatesDto;
import org.zxx17.logistics.domain.entity.WorkflowStates;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Getter
@Component
public class WorkflowStatesContainer {

  private final ConcurrentHashMap<Long, WorkflowStates> statesMap = new ConcurrentHashMap<>();

  public void addStates(List<WorkflowStates> workflowStates) {
    workflowStates.forEach(states -> statesMap.put(states.getId(), states));
  }

  public void bachDeleteByWorkflowId(Long workflowId) {
    statesMap.entrySet().removeIf(
        entry -> entry.getValue().getWorkflowId().equals(workflowId));
  }


  /**
   * 批量更新工作流状态.
   */
  public void bachUpdateByWorkflowId(Long workflowId, List<StatesDto> states) {
    statesMap.values().stream()
        .filter(state -> state.getWorkflowId().equals(workflowId))
        .forEach(state -> {
          states.forEach(statesDto -> {
            state.setStateCode(statesDto.getCode());
            state.setStateName(statesDto.getName());
            state.setUpdatedTime(new Date());
          });
        });
  }
}
