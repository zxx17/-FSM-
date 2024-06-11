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

  private Integer size = 10;

  public Integer getPage() {
    if (page == null || page < 1) {
      return 1;
    }
    return page;
  }

  public Integer getSize() {
    if (size == null || size < 1 || size > Integer.MAX_VALUE) {
      return 10;
    }
    return size;
  }


}
