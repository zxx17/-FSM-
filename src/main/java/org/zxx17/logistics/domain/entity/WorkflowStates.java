package org.zxx17.logistics.domain.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 业务流程状态表(WorkflowStates)实体类
 *
 * @author makejava
 * @since 2024-06-11 20:29:28
 */
@Data
public class WorkflowStates implements Serializable {

  private static final long serialVersionUID = 650943640314352652L;
  /**
   * 流程状态ID
   */
  private Long id;
  /**
   * 流程ID
   */
  private Long workflowId;
  /**
   * 状态编码
   */
  private String stateCode;
  /**
   * 创建时间
   */
  private Date createdTime;
  /**
   * 更新时间
   */
  private Date updatedTime;

}

