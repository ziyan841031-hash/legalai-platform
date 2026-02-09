package com.legalai.platform.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("account_ledger_entries")
public class AccountLedgerEntry {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("account_id")
    private Long accountId;

    @TableField("user_id")
    private Long userId;

    @TableField("entry_type")
    private String entryType;

    @TableField("billing_type")
    private String billingType;

    private BigDecimal amount;

    @TableField("balance_after")
    private BigDecimal balanceAfter;

    @TableField("reference_no")
    private String referenceNo;

    private String description;

    @TableField("created_at")
    private OffsetDateTime createdAt;
}
