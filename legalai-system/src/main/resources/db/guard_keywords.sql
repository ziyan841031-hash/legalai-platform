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

insert into guard_keywords (keyword, risk_level, category, enabled)
values
    ('炸弹', 'HIGH', '暴力', true),
    ('爆炸', 'HIGH', '暴力', true),
    ('枪支', 'HIGH', '暴力', true),
    ('自杀', 'HIGH', '自残', true),
    ('杀人', 'HIGH', '暴力', true),
    ('恐怖', 'HIGH', '极端主义', true),
    ('毒品', 'HIGH', '非法物品', true),
    ('走私', 'HIGH', '非法物品', true),
    ('诈骗', 'HIGH', '违法犯罪', true),
    ('黑客', 'HIGH', '违法犯罪', true);
