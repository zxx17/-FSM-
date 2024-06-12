package org.zxx17.logistics.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
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

}
