package org.zxx17.logistics.container;

import java.util.Date;
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

  public void deleteById(Long workflowId) {
    workflowsMap.remove(workflowId);
  }

  /**
   * 更新.
   */
  public void updateById(Long workflowId, String name, String desc) {
    workflowsMap.computeIfPresent(workflowId, (k, v) -> {
      v.setName(name);
      v.setDescription(desc);
      v.setUpdatedTime(new Date());
      return v;
    });


  }

  /**
   * 根据 appId 和 name 查询.
   */
  public boolean getWorkflowsByAppIdAndName(Long appId, String name) {
    return workflowsMap.values().stream().anyMatch(
        workflows -> workflows.getApplicationId().equals(appId)
            && workflows.getName().equals(name));
  }

  /**
   * 找出appId 和 name 相同的，但是排除自己.
   */
  public boolean getWorkflowsByAppIdAndNameExcludeSelf(
      Long appId, String name, Long workflowId) {
    return workflowsMap.values().stream().anyMatch(
        workflows -> workflows.getApplicationId().equals(appId)
            && workflows.getName().equals(name)
            && !workflows.getId().equals(workflowId));
  }
}
