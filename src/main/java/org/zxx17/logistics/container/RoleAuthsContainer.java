package org.zxx17.logistics.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.domain.entity.RoleAuths;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Getter
@Component
public class RoleAuthsContainer {
  private final ConcurrentHashMap<Long, RoleAuths> roleAuthsMap = new ConcurrentHashMap<>();

  public void addRoleAuths(List<RoleAuths> roleAuths) {
    roleAuths.forEach(roleAuth -> roleAuthsMap.put(roleAuth.getId(), roleAuth));
  }

}
