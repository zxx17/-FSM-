package org.zxx17.logistics.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zxx17.logistics.domain.entity.Roles;

/**
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Mapper
public interface RolesMapper {

  int insertAppRoles(@Param("rolesList") List<Roles> rolesList);
}
