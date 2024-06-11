package org.zxx17.logistics.controller.request;

import java.util.List;
import lombok.Data;
import org.zxx17.logistics.domain.dto.RolesDTO;
import org.zxx17.logistics.domain.dto.StatesDTO;

/**
 * 创建业务应用请求实体.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Data
public class CreateBusinessAppRequest {
  private String name; // 必填，应用名称
  private String desc; // 非必填，应用描述
  private List<StatesDTO> states; // 必填，状态列表
  private String beginState; // 必填，开始状态
  private String endState; // 必填，结束状态
  private List<RolesDTO> roles; // 必填，角色列表
}
