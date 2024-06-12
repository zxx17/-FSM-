package org.zxx17.logistics.container;

import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.domain.entity.Workflows;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Getter
@Component
public class WorkflowContainer {
  private final ConcurrentHashMap<Long, Workflows> workflowsMap = new ConcurrentHashMap<>();
  public boolean addWorkflows(Workflows workflows) {
    return workflowsMap.putIfAbsent(workflows.getId(), workflows) == null;
  }

}
