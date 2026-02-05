package com.legalai.platform.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.legalai.platform.userservice.domain.UserPaymentOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPaymentOrderMapper extends BaseMapper<UserPaymentOrder> {
}
