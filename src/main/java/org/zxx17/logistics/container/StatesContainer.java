package org.zxx17.logistics.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.domain.entity.States;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Getter
@Component
public class StatesContainer {


  private final ConcurrentHashMap<Long, States> statesMap = new ConcurrentHashMap<>();

  public void addStates(List<States> states) {
    states.forEach(state -> statesMap.put(state.getId(), state));
  }

}
