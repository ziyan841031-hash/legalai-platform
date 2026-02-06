-- 高危关键字码表
create table if not exists guard_keywords (
    id bigserial primary key,
    keyword varchar(128) not null,
    risk_level varchar(32) not null,
    category varchar(64),
    enabled boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- 组织配置表
CREATE TABLE org_configurations (
  id BIGSERIAL PRIMARY KEY,
  org_id BIGINT NOT NULL,
  config_key VARCHAR(128) NOT NULL,
  config_value TEXT NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE org_configurations IS '组织配置表';
COMMENT ON COLUMN org_configurations.id IS '主键ID';
COMMENT ON COLUMN org_configurations.org_id IS '组织ID';
COMMENT ON COLUMN org_configurations.config_key IS '配置键';
COMMENT ON COLUMN org_configurations.config_value IS '配置值';
COMMENT ON COLUMN org_configurations.status IS '状态';
COMMENT ON COLUMN org_configurations.created_at IS '创建时间';
COMMENT ON COLUMN org_configurations.updated_at IS '更新时间';

-- 组织表
CREATE TABLE organizations (
  id BIGSERIAL PRIMARY KEY,
  org_code VARCHAR(64) UNIQUE NOT NULL,
  org_name VARCHAR(255) NOT NULL,
  org_type VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  parent_id BIGINT,
  contact_phone VARCHAR(64),
  contact_email VARCHAR(255),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE organizations IS '组织表';
COMMENT ON COLUMN organizations.id IS '主键ID';
COMMENT ON COLUMN organizations.org_code IS '组织编码';
COMMENT ON COLUMN organizations.org_name IS '组织名称';
COMMENT ON COLUMN organizations.org_type IS '组织类型';
COMMENT ON COLUMN organizations.status IS '状态';
COMMENT ON COLUMN organizations.parent_id IS '上级组织ID';
COMMENT ON COLUMN organizations.contact_phone IS '联系电话';
COMMENT ON COLUMN organizations.contact_email IS '联系邮箱';
COMMENT ON COLUMN organizations.created_at IS '创建时间';
COMMENT ON COLUMN organizations.updated_at IS '更新时间';

-- 组织成员关系表
CREATE TABLE organization_memberships (
  id BIGSERIAL PRIMARY KEY,
  org_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  role_code VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  joined_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (org_id, user_id)
);

COMMENT ON TABLE organization_memberships IS '组织成员关系表';
COMMENT ON COLUMN organization_memberships.id IS '主键ID';
COMMENT ON COLUMN organization_memberships.org_id IS '组织ID';
COMMENT ON COLUMN organization_memberships.user_id IS '用户ID';
COMMENT ON COLUMN organization_memberships.role_code IS '角色编码';
COMMENT ON COLUMN organization_memberships.status IS '状态';
COMMENT ON COLUMN organization_memberships.joined_at IS '加入时间';
COMMENT ON COLUMN organization_memberships.created_at IS '创建时间';
COMMENT ON COLUMN organization_memberships.updated_at IS '更新时间';

-- 用户表
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  user_code VARCHAR(64) UNIQUE NOT NULL,
  full_name VARCHAR(128) NOT NULL,
  mobile VARCHAR(32),
  email VARCHAR(255),
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  openid VARCHAR(128),
  miniapp_appid VARCHAR(64),
  id_number VARCHAR(64),
  id_type VARCHAR(32),
  occupation VARCHAR(64),
  user_level VARCHAR(32),
  user_type VARCHAR(32),
  org_id BIGINT,
  open_type VARCHAR(32),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS '主键ID';
COMMENT ON COLUMN users.user_code IS '用户编码';
COMMENT ON COLUMN users.full_name IS '用户姓名';
COMMENT ON COLUMN users.mobile IS '手机号';
COMMENT ON COLUMN users.email IS '邮箱';
COMMENT ON COLUMN users.status IS '状态';
COMMENT ON COLUMN users.openid IS '微信openid';
COMMENT ON COLUMN users.miniapp_appid IS '小程序appid';
COMMENT ON COLUMN users.id_number IS '证件号';
COMMENT ON COLUMN users.id_type IS '证件类型';
COMMENT ON COLUMN users.occupation IS '职业';
COMMENT ON COLUMN users.user_level IS '用户等级';
COMMENT ON COLUMN users.user_type IS '用户类型(个人/组织)';
COMMENT ON COLUMN users.org_id IS '组织ID';
COMMENT ON COLUMN users.open_type IS '用户开通类型(默认积分/订阅)';
COMMENT ON COLUMN users.created_at IS '创建时间';
COMMENT ON COLUMN users.updated_at IS '更新时间';

-- 用户订阅情况表
CREATE TABLE user_subscriptions (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  plan_code VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  started_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  expires_at TIMESTAMPTZ NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE user_subscriptions IS '用户订阅情况表';
COMMENT ON COLUMN user_subscriptions.id IS '主键ID';
COMMENT ON COLUMN user_subscriptions.user_id IS '用户ID';
COMMENT ON COLUMN user_subscriptions.plan_code IS '订阅方案编码';
COMMENT ON COLUMN user_subscriptions.status IS '订阅状态';
COMMENT ON COLUMN user_subscriptions.started_at IS '订阅开始时间';
COMMENT ON COLUMN user_subscriptions.expires_at IS '订阅到期时间';
COMMENT ON COLUMN user_subscriptions.created_at IS '创建时间';
COMMENT ON COLUMN user_subscriptions.updated_at IS '更新时间';

-- 用户账户表
CREATE TABLE user_accounts (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  account_type VARCHAR(32) NOT NULL,
  balance_type VARCHAR(32) NOT NULL,
  tool_ids TEXT,
  balance NUMERIC(18, 2) NOT NULL DEFAULT 0,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  currency VARCHAR(16) NOT NULL DEFAULT 'CNY',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (user_id, account_type, balance_type)
);

COMMENT ON TABLE user_accounts IS '用户账户表';
COMMENT ON COLUMN user_accounts.id IS '主键ID';
COMMENT ON COLUMN user_accounts.user_id IS '用户ID';
COMMENT ON COLUMN user_accounts.account_type IS '账户类型';
COMMENT ON COLUMN user_accounts.balance_type IS '余额类型(积分/次数)';
COMMENT ON COLUMN user_accounts.tool_ids IS '工具ID列表';
COMMENT ON COLUMN user_accounts.balance IS '余额';
COMMENT ON COLUMN user_accounts.status IS '状态';
COMMENT ON COLUMN user_accounts.currency IS '币种';
COMMENT ON COLUMN user_accounts.created_at IS '创建时间';
COMMENT ON COLUMN user_accounts.updated_at IS '更新时间';

-- 用户二级账户表
CREATE TABLE user_account_balances (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT NOT NULL,
  balance_type VARCHAR(32) NOT NULL,
  balance NUMERIC(18, 2) NOT NULL,
  expires_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE user_account_balances IS '用户二级账户表';
COMMENT ON COLUMN user_account_balances.id IS '主键ID';
COMMENT ON COLUMN user_account_balances.account_id IS '主账户ID';
COMMENT ON COLUMN user_account_balances.balance_type IS '余额类型';
COMMENT ON COLUMN user_account_balances.balance IS '余额';
COMMENT ON COLUMN user_account_balances.expires_at IS '失效时间';
COMMENT ON COLUMN user_account_balances.created_at IS '创建时间';

-- 账户流水表
CREATE TABLE account_ledger_entries (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  entry_type VARCHAR(32) NOT NULL,
  billing_type VARCHAR(32) NOT NULL,
  amount NUMERIC(18, 2) NOT NULL,
  balance_after NUMERIC(18, 2) NOT NULL,
  reference_no VARCHAR(64),
  description TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE account_ledger_entries IS '账户流水表';
COMMENT ON COLUMN account_ledger_entries.id IS '主键ID';
COMMENT ON COLUMN account_ledger_entries.account_id IS '账户ID';
COMMENT ON COLUMN account_ledger_entries.user_id IS '用户ID';
COMMENT ON COLUMN account_ledger_entries.entry_type IS '流水类型';
COMMENT ON COLUMN account_ledger_entries.billing_type IS '计费类型(积分/每次)';
COMMENT ON COLUMN account_ledger_entries.amount IS '发生金额';
COMMENT ON COLUMN account_ledger_entries.balance_after IS '变更后余额';
COMMENT ON COLUMN account_ledger_entries.reference_no IS '关联单号';
COMMENT ON COLUMN account_ledger_entries.description IS '备注';
COMMENT ON COLUMN account_ledger_entries.created_at IS '创建时间';

-- 用户支付订单表
CREATE TABLE user_payment_orders (
  id BIGSERIAL PRIMARY KEY,
  order_no VARCHAR(64) UNIQUE NOT NULL,
  user_id BIGINT NOT NULL,
  org_id BIGINT,
  amount NUMERIC(18, 2) NOT NULL,
  status VARCHAR(32) NOT NULL,
  channel VARCHAR(32),
  paid_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE user_payment_orders IS '用户支付订单表';
COMMENT ON COLUMN user_payment_orders.id IS '主键ID';
COMMENT ON COLUMN user_payment_orders.order_no IS '订单号';
COMMENT ON COLUMN user_payment_orders.user_id IS '用户ID';
COMMENT ON COLUMN user_payment_orders.org_id IS '组织ID';
COMMENT ON COLUMN user_payment_orders.amount IS '支付金额';
COMMENT ON COLUMN user_payment_orders.status IS '订单状态';
COMMENT ON COLUMN user_payment_orders.channel IS '支付渠道';
COMMENT ON COLUMN user_payment_orders.paid_at IS '支付时间';
COMMENT ON COLUMN user_payment_orders.created_at IS '创建时间';
COMMENT ON COLUMN user_payment_orders.updated_at IS '更新时间';

-- 历史记录表(集)
CREATE TABLE history_records (
  id BIGSERIAL PRIMARY KEY,
  org_id BIGINT,
  user_id BIGINT NOT NULL,
  record_type VARCHAR(64) NOT NULL,
  record_payload JSONB NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE history_records IS '历史记录表(集)';
COMMENT ON COLUMN history_records.id IS '主键ID';
COMMENT ON COLUMN history_records.org_id IS '组织ID';
COMMENT ON COLUMN history_records.user_id IS '用户ID';
COMMENT ON COLUMN history_records.record_type IS '记录类型';
COMMENT ON COLUMN history_records.record_payload IS '记录内容';
COMMENT ON COLUMN history_records.created_at IS '创建时间';

-- 案件管理表(集)
CREATE TABLE case_records (
  id BIGSERIAL PRIMARY KEY,
  org_id BIGINT,
  case_no VARCHAR(64) NOT NULL,
  title VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL,
  payload JSONB NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE case_records IS '案件管理表(集)';
COMMENT ON COLUMN case_records.id IS '主键ID';
COMMENT ON COLUMN case_records.org_id IS '组织ID';
COMMENT ON COLUMN case_records.case_no IS '案件编号';
COMMENT ON COLUMN case_records.title IS '案件标题';
COMMENT ON COLUMN case_records.status IS '案件状态';
COMMENT ON COLUMN case_records.payload IS '案件内容';
COMMENT ON COLUMN case_records.created_at IS '创建时间';
COMMENT ON COLUMN case_records.updated_at IS '更新时间';

-- 文档管理表(集)
CREATE TABLE document_records (
  id BIGSERIAL PRIMARY KEY,
  org_id BIGINT,
  doc_no VARCHAR(64) NOT NULL,
  title VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL,
  payload JSONB NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE document_records IS '文档管理表(集)';
COMMENT ON COLUMN document_records.id IS '主键ID';
COMMENT ON COLUMN document_records.org_id IS '组织ID';
COMMENT ON COLUMN document_records.doc_no IS '文档编号';
COMMENT ON COLUMN document_records.title IS '文档标题';
COMMENT ON COLUMN document_records.status IS '文档状态';
COMMENT ON COLUMN document_records.payload IS '文档内容';
COMMENT ON COLUMN document_records.created_at IS '创建时间';
COMMENT ON COLUMN document_records.updated_at IS '更新时间';

-- 工具权限配置表
CREATE TABLE tool_permission_configs (
  id BIGSERIAL PRIMARY KEY,
  tool_code VARCHAR(64) NOT NULL,
  role_code VARCHAR(64) NOT NULL,
  org_id BIGINT,
  permission_level VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (tool_code, role_code, org_id)
);

COMMENT ON TABLE tool_permission_configs IS '工具权限配置表';
COMMENT ON COLUMN tool_permission_configs.id IS '主键ID';
COMMENT ON COLUMN tool_permission_configs.tool_code IS '工具编码';
COMMENT ON COLUMN tool_permission_configs.role_code IS '角色编码';
COMMENT ON COLUMN tool_permission_configs.org_id IS '组织ID';
COMMENT ON COLUMN tool_permission_configs.permission_level IS '权限等级';
COMMENT ON COLUMN tool_permission_configs.status IS '状态';
COMMENT ON COLUMN tool_permission_configs.created_at IS '创建时间';
COMMENT ON COLUMN tool_permission_configs.updated_at IS '更新时间';

-- 工具配置表
CREATE TABLE tool_configs (
  id BIGSERIAL PRIMARY KEY,
  tool_code VARCHAR(64) UNIQUE NOT NULL,
  tool_name VARCHAR(255) NOT NULL,
  category VARCHAR(64),
  intent VARCHAR(128),
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  config JSONB NOT NULL DEFAULT '{}'::jsonb,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE tool_configs IS '工具配置表';
COMMENT ON COLUMN tool_configs.id IS '主键ID';
COMMENT ON COLUMN tool_configs.tool_code IS '工具编码';
COMMENT ON COLUMN tool_configs.tool_name IS '工具名称';
COMMENT ON COLUMN tool_configs.category IS '工具分类';
COMMENT ON COLUMN tool_configs.intent IS '意图标识';
COMMENT ON COLUMN tool_configs.status IS '状态';
COMMENT ON COLUMN tool_configs.config IS '配置内容';
COMMENT ON COLUMN tool_configs.created_at IS '创建时间';
COMMENT ON COLUMN tool_configs.updated_at IS '更新时间';

-- 工具计费配置表
CREATE TABLE tool_pricing_configs (
  id BIGSERIAL PRIMARY KEY,
  tool_code VARCHAR(64) NOT NULL,
  pricing_type VARCHAR(32) NOT NULL,
  unit_price NUMERIC(18, 2) NOT NULL,
  currency VARCHAR(16) NOT NULL DEFAULT 'CNY',
  billing_rule JSONB NOT NULL DEFAULT '{}'::jsonb,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE tool_pricing_configs IS '工具计费配置表';
COMMENT ON COLUMN tool_pricing_configs.id IS '主键ID';
COMMENT ON COLUMN tool_pricing_configs.tool_code IS '工具编码';
COMMENT ON COLUMN tool_pricing_configs.pricing_type IS '计费类型';
COMMENT ON COLUMN tool_pricing_configs.unit_price IS '单价';
COMMENT ON COLUMN tool_pricing_configs.currency IS '币种';
COMMENT ON COLUMN tool_pricing_configs.billing_rule IS '计费规则';
COMMENT ON COLUMN tool_pricing_configs.status IS '状态';
COMMENT ON COLUMN tool_pricing_configs.created_at IS '创建时间';
COMMENT ON COLUMN tool_pricing_configs.updated_at IS '更新时间';

-- 工具实施表(集)
CREATE TABLE tool_implementations (
  id BIGSERIAL PRIMARY KEY,
  tool_code VARCHAR(64) NOT NULL,
  org_id BIGINT,
  implementation_payload JSONB NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE tool_implementations IS '工具实施表(集)';
COMMENT ON COLUMN tool_implementations.id IS '主键ID';
COMMENT ON COLUMN tool_implementations.tool_code IS '工具编码';
COMMENT ON COLUMN tool_implementations.org_id IS '组织ID';
COMMENT ON COLUMN tool_implementations.implementation_payload IS '实施内容';
COMMENT ON COLUMN tool_implementations.status IS '状态';
COMMENT ON COLUMN tool_implementations.created_at IS '创建时间';
COMMENT ON COLUMN tool_implementations.updated_at IS '更新时间';

ALTER TABLE org_configurations ADD CONSTRAINT fk_org_configurations_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE organization_memberships ADD CONSTRAINT fk_organization_memberships_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE organization_memberships ADD CONSTRAINT fk_organization_memberships_user
  FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users ADD CONSTRAINT fk_users_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE user_subscriptions ADD CONSTRAINT fk_user_subscriptions_user
  FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_accounts ADD CONSTRAINT fk_user_accounts_user
  FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_account_balances ADD CONSTRAINT fk_user_account_balances_account
  FOREIGN KEY (account_id) REFERENCES user_accounts (id);

ALTER TABLE account_ledger_entries ADD CONSTRAINT fk_account_ledger_entries_account
  FOREIGN KEY (account_id) REFERENCES user_accounts (id);

ALTER TABLE account_ledger_entries ADD CONSTRAINT fk_account_ledger_entries_user
  FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_payment_orders ADD CONSTRAINT fk_user_payment_orders_user
  FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_payment_orders ADD CONSTRAINT fk_user_payment_orders_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE history_records ADD CONSTRAINT fk_history_records_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE history_records ADD CONSTRAINT fk_history_records_user
  FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE case_records ADD CONSTRAINT fk_case_records_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE document_records ADD CONSTRAINT fk_document_records_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE tool_permission_configs ADD CONSTRAINT fk_tool_permission_configs_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

ALTER TABLE tool_pricing_configs ADD CONSTRAINT fk_tool_pricing_configs_tool
  FOREIGN KEY (tool_code) REFERENCES tool_configs (tool_code);

ALTER TABLE tool_implementations ADD CONSTRAINT fk_tool_implementations_tool
  FOREIGN KEY (tool_code) REFERENCES tool_configs (tool_code);

ALTER TABLE tool_implementations ADD CONSTRAINT fk_tool_implementations_org
  FOREIGN KEY (org_id) REFERENCES organizations (id);

insert into guard_keywords (keyword, risk_level, category, enabled)
values
    ('炸弹', 'HIGH', '暴力犯罪', true),
    ('爆炸', 'HIGH', '暴力犯罪', true),
    ('枪支', 'HIGH', '暴力犯罪', true),
    ('自杀', 'HIGH', '自残自杀', true),
    ('杀人', 'HIGH', '暴力犯罪', true),
    ('恐怖', 'HIGH', '极端主义', true),
    ('毒品', 'HIGH', '毒品犯罪', true),
    ('走私', 'HIGH', '走私犯罪', true),
    ('诈骗', 'HIGH', '金融犯罪', true),
    ('黑客', 'HIGH', '网络犯罪', true),
    ('伪造公章', 'HIGH', '司法犯罪', true),
    ('伪造证件', 'HIGH', '司法犯罪', true),
    ('假证', 'HIGH', '司法犯罪', true),
    ('洗钱', 'HIGH', '金融犯罪', true),
    ('逃税', 'HIGH', '金融犯罪', true),
    ('贿赂', 'HIGH', '职务犯罪', true),
    ('行贿', 'HIGH', '职务犯罪', true),
    ('受贿', 'HIGH', '职务犯罪', true),
    ('内幕交易', 'HIGH', '证券犯罪', true),
    ('操纵市场', 'HIGH', '证券犯罪', true),
    ('非法集资', 'HIGH', '金融犯罪', true),
    ('赌博', 'HIGH', '治安犯罪', true),
    ('卖淫', 'HIGH', '治安犯罪', true),
    ('嫖娼', 'HIGH', '治安犯罪', true),
    ('淫秽', 'HIGH', '涉黄', true),
    ('儿童色情', 'HIGH', '涉黄', true),
    ('偷拍', 'HIGH', '隐私侵害', true),
    ('勒索', 'HIGH', '暴力犯罪', true),
    ('敲诈', 'HIGH', '暴力犯罪', true),
    ('非法拘禁', 'HIGH', '暴力犯罪', true),
    ('绑架', 'HIGH', '暴力犯罪', true),
    ('伪证', 'HIGH', '司法犯罪', true),
    ('毁灭证据', 'HIGH', '司法犯罪', true),
    ('伪造合同', 'HIGH', '经济犯罪', true),
    ('偷税漏税', 'HIGH', '金融犯罪', true),
    ('侵犯隐私', 'HIGH', '隐私侵害', true),
    ('泄露隐私', 'HIGH', '隐私侵害', true),
    ('非法获取公民信息', 'HIGH', '隐私侵害', true),
    ('违法取证', 'HIGH', '司法犯罪', true),
    ('教唆犯罪', 'HIGH', '暴力犯罪', true),
    ('制毒', 'HIGH', '毒品犯罪', true),
    ('贩毒', 'HIGH', '毒品犯罪', true),
    ('制枪', 'HIGH', '暴力犯罪', true),
    ('买卖枪支', 'HIGH', '暴力犯罪', true),
    ('爆炸物', 'HIGH', '暴力犯罪', true),
    ('危险品运输', 'HIGH', '公共安全', true),
    ('非法传销', 'HIGH', '经济犯罪', true),
    ('虚开发票', 'HIGH', '经济犯罪', true),
    ('伪造发票', 'HIGH', '经济犯罪', true),
    ('传销', 'HIGH', '经济犯罪', true);
