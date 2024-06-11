package org.zxx17.logistics.domain.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 角色表(Roles)实体类
 *
 * @author makejava
 * @since 2024-06-11 20:29:28
 */
@Data
public class Roles implements Serializable {

  private static final long serialVersionUID = -76943500714563704L;
  /**
   * 角色ID
   */
  private Long id;
  /**
   * 应用ID
   */
  private Long applicationId;
  /**
   * 角色名称
   */
  private String roleName;
  /**
   * 创建时间
   */
  private Date createdTime;
  /**
   * 更新时间
   */
  private Date updatedTime;


}

