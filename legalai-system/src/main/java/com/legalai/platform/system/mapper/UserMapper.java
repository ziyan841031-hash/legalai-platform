package com.legalai.platform.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.legalai.platform.system.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
