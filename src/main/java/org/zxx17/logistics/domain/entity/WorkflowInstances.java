package org.zxx17.logistics.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 业务流程实例表(WorkflowInstances)实体类.
 *
 * @author makejava
 * @since 2024-06-11 20:29:28
 */
@Data
public class WorkflowInstances implements Serializable {

  private static final long serialVersionUID = -76961881949123811L;
  /**
   * 实例ID.
   */
  private Long id;
  /**
   * 流程ID.
   */
  private Long workflowId;
  /**
   * 当前状态编码.
   */
  private String currentState;
  /**
   * 前一个状态编码.
   */
  private String previousState;
  /**
   * 历史状态记录.
   */
  private String history;
  /**
   * 创建时间.
   */
  private Date createdTime;
  /**
   * 更新时间.
   */
  private Date updatedTime;

}

