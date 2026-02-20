package com.legalai.platform.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.legalai.platform.system.domain.HistoryRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HistoryRecordMapper extends BaseMapper<HistoryRecord> {
}
