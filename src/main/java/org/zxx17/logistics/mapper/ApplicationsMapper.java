package org.zxx17.logistics.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.zxx17.logistics.domain.entity.Applications;

/**
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Mapper
public interface ApplicationsMapper {

  int createApp(Applications applications);
}
