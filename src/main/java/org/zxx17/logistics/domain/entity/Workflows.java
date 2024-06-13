package org.zxx17.logistics.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 业务流程表(Workflows)实体类.
 *
 * @author Xinxuan Zhuo
 * @since 2024-06-11 20:29:28
 */
@Data
public class Workflows implements Serializable {

  private static final long serialVersionUID = -33569661324294031L;
  /**
   * 流程ID.
   */
  private Long id;
  /**
   * 应用ID.
   */
  private Long applicationId;
  /**
   * 流程名称.
   */
  private String name;
  /**
   * 创建者.
   */
  private String creator;
  /**
   * 流程描述.
   */
  private String description;
  /**
   * 创建时间.
   */
  private Date createdTime;
  /**
   * 更新时间.
   */
  private Date updatedTime;


}

