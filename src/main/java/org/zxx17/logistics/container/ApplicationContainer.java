package org.zxx17.logistics.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.domain.entity.Applications;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Getter
@Component
public class ApplicationContainer {

  private final ConcurrentHashMap<Long, Applications> applicationsMap = new ConcurrentHashMap<>();

  /**
   * 添加应用 .
   */
  public void addApplication(Applications applications) {
    if (applicationsMap.containsKey(applications.getId())) {
      return;
    }
    applicationsMap.put(applications.getId(), applications);
  }


}
