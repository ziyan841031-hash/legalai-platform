package com.legalai.platform.userservice.domain;

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
@TableName(value = "tool_implementations", autoResultMap = true)
public class ToolImplementation {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("tool_code")
    private String toolCode;

    @TableField("org_id")
    private Long orgId;

    @TableField(value = "implementation_payload", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> implementationPayload;

    private String status;

    @TableField("created_at")
    private OffsetDateTime createdAt;

    @TableField("updated_at")
    private OffsetDateTime updatedAt;
}
