package org.zxx17.logistics.domain.dto;

import java.util.Date;
import lombok.Data;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 **/
@Data
public class WorkflowsDto {

  private Long id;
  private String name;
  private String desc;
  private String creator;
  private String createdTime;

}
