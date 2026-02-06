package com.legalai.platform.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.legalai.platform.system.domain.AccountLedgerEntry;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountLedgerEntryMapper extends BaseMapper<AccountLedgerEntry> {
}
