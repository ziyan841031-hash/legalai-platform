package com.legalai.platform.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tool_permission_configs")
public class ToolPermissionConfig {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("tool_code")
    private String toolCode;

    @TableField("role_code")
    private String roleCode;

    @TableField("org_id")
    private Long orgId;

    @TableField("permission_level")
    private String permissionLevel;

    private String status;

    @TableField("created_at")
    private OffsetDateTime createdAt;

    @TableField("updated_at")
    private OffsetDateTime updatedAt;
}
