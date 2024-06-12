package org.zxx17.logistics.container;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.domain.dto.WorkflowEventsDto;
import org.zxx17.logistics.domain.entity.WorkflowEvents;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Getter
@Component
public class WorkflowEventContainer {

  private final ConcurrentHashMap<Long, WorkflowEvents> workflowEventsMap =
      new ConcurrentHashMap<>();

  /**
   * 添加工作流事件.
   */
  public void addWorkflowEvents(List<WorkflowEvents> workflowEventsList) {
    workflowEventsList.forEach(
        workflowEvents -> workflowEventsMap.put(workflowEvents.getId(), workflowEvents)
    );
  }

  /**
   * 批量删除工作流事件.
   */
  public void bachDeleteByWorkflowId(Long workflowId) {
    workflowEventsMap.entrySet().removeIf(
        entry -> entry.getValue().getWorkflowId().equals(workflowId)
    );
  }

  /**
   * 批量更新工作流事件.
   */
  public void batchUpdateByWorkflowId(Long workflowId, List<WorkflowEventsDto> events) {
    workflowEventsMap.values().stream()
        .filter(event -> event.getWorkflowId().equals(workflowId))
        .forEach(event -> {
          events.forEach(eventDto -> {
            event.setEventName(eventDto.getName());
            event.setFromState(eventDto.getFromState());
            event.setToState(eventDto.getToState());
            event.setRole(eventDto.getRole());
            event.setUpdatedTime(new Date());
          });
        });
  }



}
