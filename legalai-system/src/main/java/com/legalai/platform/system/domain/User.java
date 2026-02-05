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
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_code")
    private String userCode;

    @TableField("full_name")
    private String fullName;

    private String mobile;

    private String email;

    private String status;

    private String openid;

    @TableField("miniapp_appid")
    private String miniappAppid;

    @TableField("id_number")
    private String idNumber;

    @TableField("id_type")
    private String idType;

    private String occupation;

    @TableField("user_level")
    private String userLevel;

    @TableField("user_type")
    private String userType;

    @TableField("org_id")
    private Long orgId;

    @TableField("open_type")
    private String openType;

    @TableField("created_at")
    private OffsetDateTime createdAt;

    @TableField("updated_at")
    private OffsetDateTime updatedAt;
}
