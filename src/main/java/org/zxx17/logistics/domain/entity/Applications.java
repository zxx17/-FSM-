package org.zxx17.logistics.domain.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 业务应用表(Applications)实体类
 *
 * @author makejava
 * @since 2024-06-11 20:29:27
 */
@Data
public class Applications implements Serializable {

  private static final long serialVersionUID = -99303431849944445L;
  /**
   * 应用ID
   */
  private Long id;
  /**
   * 应用名称
   */
  private String name;
  /**
   * 应用描述
   */
  private String description;
  /**
   * 开始状态编码
   */
  private String startState;
  /**
   * 结束状态编码
   */
  private String endState;
  /**
   * 创建时间
   */
  private Date createdTime;
  /**
   * 更新时间
   */
  private Date updatedTime;




}

