package org.zxx17.logistics.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.common.util.id.SnowFlake;
import org.zxx17.logistics.common.util.id.SnowFlakeFactory;
import org.zxx17.logistics.controller.request.CreateBusinessAppRequest;
import org.zxx17.logistics.domain.dto.RolesDTO;
import org.zxx17.logistics.domain.entity.Applications;
import org.zxx17.logistics.domain.entity.RoleAuths;
import org.zxx17.logistics.domain.entity.Roles;
import org.zxx17.logistics.domain.entity.States;
import org.zxx17.logistics.manager.BusinessManger;
import org.zxx17.logistics.mapper.ApplicationsMapper;
import org.zxx17.logistics.mapper.RoleAuthsMapper;
import org.zxx17.logistics.mapper.RolesMapper;
import org.zxx17.logistics.mapper.StatesMapper;
import org.zxx17.logistics.mapper.WorkflowStatesMapper;

/**
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessMangerImpl implements BusinessManger {

  private final ApplicationsMapper applicationsMapper;

  private final RolesMapper rolesMapper;

  private final RoleAuthsMapper roleAuthsMapper;

  private final StatesMapper statesMapper;


  @Override
  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
  public Result<Long> handleCreateBusinessApp(CreateBusinessAppRequest request) {
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
    int row = applicationsMapper.createApp(applications);

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
    int row2 = statesMapper.insertAppStates(stateList);

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
    int row3 = rolesMapper.insertAppRoles(rolesList);
    int row4 = roleAuthsMapper.insertAppRoleAuths(roleAuthsList);

    return Result.response(bizAppId, ResultEnum.SUCCESS, "流程创建成功");
  }


}
