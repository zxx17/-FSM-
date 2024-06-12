package org.zxx17.logistics.domain.dto;

import lombok.Data;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
@Data
public class RoleAuthDto {

  /**
   * 起始状态编码.
   */
  private String fromState;
  /**
   * 目标状态编码.
   */
  private String toState;

}
