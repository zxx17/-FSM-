package org.zxx17.logistics.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
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
}
