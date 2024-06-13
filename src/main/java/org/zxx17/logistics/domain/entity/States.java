package org.zxx17.logistics.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 状态表(States)实体类.
 *
 * @author Xinxuan Zhuo
 * @since 2024-06-11 20:29:28
 */
@Data
public class States implements Serializable {

  private static final long serialVersionUID = 682367917671922484L;
  /**
   * 状态ID.
   */
  private Long id;
  /**
   * 应用ID.
   */
  private Long applicationId;
  /**
   * 状态编码.
   */
  private String code;
  /**
   * 状态名称.
   */
  private String name;
  /**
   * 创建时间.
   */
  private Date createdTime;
  /**
   * 更新时间.
   */
  private Date updatedTime;

}

