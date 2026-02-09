package com.legalai.platform.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "history_records", autoResultMap = true)
public class HistoryRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("org_id")
    private Long orgId;

    @TableField("user_id")
    private Long userId;

    @TableField("record_type")
    private String recordType;

    @TableField(value = "record_payload", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> recordPayload;

    @TableField("created_at")
    private OffsetDateTime createdAt;
}
