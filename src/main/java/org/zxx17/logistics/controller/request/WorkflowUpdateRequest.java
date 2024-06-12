package org.zxx17.logistics.controller.request;

import java.util.List;
import lombok.Data;
import org.zxx17.logistics.domain.dto.StatesDto;
import org.zxx17.logistics.domain.dto.WorkflowEventsDto;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Data
public class WorkflowUpdateRequest {
  // 流程id
  private Long id;
  // 流程名字
  private String name;
  // 流程描述
  private String desc;
  // 状态列表
  private List<StatesDto> states;
  // 事件列表
  private List<WorkflowEventsDto> events;

}
