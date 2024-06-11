package org.zxx17.logistics.common.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
@Data
public class PageResult<T> implements Serializable {

  private Integer pageNo = 1;

  private Integer pageSize = 20;

  private Integer total = 0;

  private Integer totalPages = 0;

  private List<T> content = Collections.emptyList();

  private Integer start = 1;

  private Integer end = 0;

  public void setRecords(List<T> content) {
    this.content = content;
    if (content != null && content.size() > 0) {
      setTotal(content.size());
    }
  }

  public void setTotal(Integer total) {
    this.total = total;
    if (this.pageSize > 0) {
      this.totalPages = (total / this.pageSize) + (total % this.pageSize == 0 ? 0 : 1);
    } else {
      this.totalPages = 0;
    }
    this.start = (this.pageSize > 0 ? (this.pageNo - 1) * this.pageSize : 0) + 1;
    this.end = (this.start - 1 + this.pageSize * (this.pageNo > 0 ? 1 : 0));
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

}
