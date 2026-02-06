package com.legalai.platform.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.legalai.platform.system.domain.UserPaymentOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPaymentOrderMapper extends BaseMapper<UserPaymentOrder> {
}
