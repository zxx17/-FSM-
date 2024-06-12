package org.zxx17.logistics.domain.dto;

import java.util.List;
import lombok.Data;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Data
public class RolesDTO {

  /**
   * 角色名称.
   */
  private String role;

  /**
   * 角色权限列表.
   */
  private List<RoleAuthDTO> auth;
}
