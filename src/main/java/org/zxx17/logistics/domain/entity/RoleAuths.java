package org.zxx17.logistics.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 角色权限表(RoleAuths)实体类.
 *
 * @author Xinxuan Zhuo
 * @since 2024-06-11 20:29:28
 */
@Data
public class RoleAuths implements Serializable {

  private static final long serialVersionUID = 711838880148231739L;
  /**
   * 权限ID.
   */
  private Long id;
  /**
   * 角色ID.
   */
  private Long roleId;
  /**
   * 起始状态编码.
   */
  private String fromState;
  /**
   * 目标状态编码.
   */
  private String toState;
  /**
   * 创建时间.
   */
  private Date createdTime;
  /**
   * 更新时间.
   */
  private Date updatedTime;

}

