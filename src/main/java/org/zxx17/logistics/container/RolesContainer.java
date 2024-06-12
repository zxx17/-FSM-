package org.zxx17.logistics.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.domain.entity.Roles;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Getter
@Component
public class RolesContainer {

  private final ConcurrentHashMap<Long, Roles> rolesMap = new ConcurrentHashMap<>();

  public void addRoles(List<Roles> roles) {
    roles.forEach(role -> rolesMap.put(role.getId(), role));
  }

}
