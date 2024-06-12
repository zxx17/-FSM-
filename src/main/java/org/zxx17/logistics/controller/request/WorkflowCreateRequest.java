package org.zxx17.logistics.controller.request;

import java.util.List;
import lombok.Data;
import org.zxx17.logistics.domain.dto.StatesDto;
import org.zxx17.logistics.domain.dto.WorkflowEventsDto;

/**
 * 创建流程请求.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Data
public class WorkflowCreateRequest {
  // 业务应用id
  private Long appId;
  // 流程名字
  private String name;
  // 流程描述
  private String desc;
  // 状态列表
  private List<StatesDto> states;
  // 事件列表
  private List<WorkflowEventsDto> events;
}
