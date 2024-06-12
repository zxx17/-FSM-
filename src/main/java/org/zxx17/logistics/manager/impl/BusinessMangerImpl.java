package org.zxx17.logistics.manager.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.common.util.id.SnowFlake;
import org.zxx17.logistics.common.util.id.SnowFlakeFactory;
import org.zxx17.logistics.container.ApplicationContainer;
import org.zxx17.logistics.container.RoleAuthsContainer;
import org.zxx17.logistics.container.RolesContainer;
import org.zxx17.logistics.container.StatesContainer;
import org.zxx17.logistics.controller.request.CreateBusinessAppRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.domain.entity.Applications;
import org.zxx17.logistics.domain.entity.RoleAuths;
import org.zxx17.logistics.domain.entity.Roles;
import org.zxx17.logistics.domain.entity.States;
import org.zxx17.logistics.manager.BusinessManger;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessMangerImpl implements BusinessManger {

  private final ApplicationContainer applicationContainer;
  private final RoleAuthsContainer roleAuthsContainer;
  private final RolesContainer rolesContainer;
  private final StatesContainer statesContainer;

  @Override
  public Result<CommonResponse> handleCreateBusinessApp(CreateBusinessAppRequest request) {
    SnowFlake snowFlake = SnowFlakeFactory.getSnowFlake();
    Long bizAppId = snowFlake.nextId();
    String beginStateCode = request.getBeginState();
    String endStateCode = request.getEndState();
    String bizAppName = request.getName();
    String bizAppDesc = request.getDesc();
    Applications applications = new Applications();
    applications.setId(bizAppId);
    applications.setName(bizAppName);
    applications.setDescription(bizAppDesc);
    applications.setStartState(beginStateCode);
    applications.setEndState(endStateCode);
    boolean flag = applicationContainer.addApplication(applications);

    List<States> stateList = new ArrayList<>();
    request.getStates().forEach(statesDTO -> {
      long stateId = snowFlake.nextId();
      States states = new States();
      states.setId(stateId);
      states.setCode(statesDTO.getCode());
      states.setName(statesDTO.getName());
      states.setApplicationId(bizAppId);
      stateList.add(states);
    });
    statesContainer.addStates(stateList);

    List<Roles> rolesList = new ArrayList<>();
    List<RoleAuths> roleAuthsList = new ArrayList<>();
    request.getRoles().forEach(rolesDTO -> {
      long roleId = snowFlake.nextId();
      Roles role = new Roles();
      role.setApplicationId(bizAppId);
      role.setId(roleId);
      role.setRoleName(rolesDTO.getRole());
      rolesList.add(role);
      rolesDTO.getAuth().forEach(roleAuthDTO -> {
        long roleAuthId = snowFlake.nextId();
        RoleAuths roleAuth = new RoleAuths();
        roleAuth.setId(roleAuthId);
        roleAuth.setRoleId(roleId);
        roleAuth.setFromState(roleAuthDTO.getFromState());
        roleAuth.setToState(roleAuthDTO.getToState());
        roleAuthsList.add(roleAuth);
      });
    });
    rolesContainer.addRoles(rolesList);
    roleAuthsContainer.addRoleAuths(roleAuthsList);

    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setId(bizAppId);
    return Result.response(commonResponse, "流程创建成功", ResultEnum.SUCCESS);
  }


}
