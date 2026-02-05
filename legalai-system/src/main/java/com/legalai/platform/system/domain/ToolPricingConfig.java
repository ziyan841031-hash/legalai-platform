package com.legalai.platform.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import java.math.BigDecimal;
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
@TableName(value = "tool_pricing_configs", autoResultMap = true)
public class ToolPricingConfig {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("tool_code")
    private String toolCode;

    @TableField("pricing_type")
    private String pricingType;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    private String currency;

    @TableField(value = "billing_rule", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> billingRule;

    private String status;

    @TableField("created_at")
    private OffsetDateTime createdAt;

    @TableField("updated_at")
    private OffsetDateTime updatedAt;
}
