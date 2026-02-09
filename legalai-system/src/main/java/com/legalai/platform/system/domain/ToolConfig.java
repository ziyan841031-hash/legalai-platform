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
@TableName(value = "tool_configs", autoResultMap = true)
public class ToolConfig {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("tool_code")
    private String toolCode;

    @TableField("tool_name")
    private String toolName;

    private String category;

    @TableField("intent")
    private String intent;

    private String status;

    @TableField(value = "config", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> config;

    @TableField("created_at")
    private OffsetDateTime createdAt;

    @TableField("updated_at")
    private OffsetDateTime updatedAt;
}
