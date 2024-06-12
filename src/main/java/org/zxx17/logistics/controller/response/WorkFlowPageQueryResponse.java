package org.zxx17.logistics.controller.response;

import java.util.List;
import lombok.Data;
import org.zxx17.logistics.domain.dto.WorkflowsDto;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 **/
@Data
public class WorkFlowPageQueryResponse {

  private Integer page;
  private Integer size;
  private Integer totalPage;
  private Integer total;
  private List<WorkflowsDto> content;
}
