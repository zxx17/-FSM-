package org.zxx17.logistics.common;

import java.io.Serializable;
import lombok.Data;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
@Data
public class PageInfo implements Serializable {

  private Integer page = 1;

  private Integer pageSize = 20;

  /**
   * 获取页码.
   */
  public Integer getPage() {
    if (page == null || page < 1) {
      return 1;
    }
    return page;
  }

  /**
   * 获取页大小.
   */
  public Integer getSize() {
    if (pageSize == null || pageSize < 1 || pageSize > Integer.MAX_VALUE) {
      return 10;
    }
    return pageSize;
  }


}
