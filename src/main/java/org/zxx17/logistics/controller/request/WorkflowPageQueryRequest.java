package org.zxx17.logistics.controller.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zxx17.logistics.common.PageInfo;

/**
 * 流程查询分页请求.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowPageQueryRequest extends PageInfo {
}
