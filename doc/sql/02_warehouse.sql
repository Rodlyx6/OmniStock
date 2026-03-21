-- ============================================================
-- WAREHOUSE表（仓库表）
-- ============================================================

CREATE TABLE warehouse (
    warehouse_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '仓库ID',
    warehouse_code VARCHAR(64) NOT NULL UNIQUE COMMENT '仓库编码',
    warehouse_name VARCHAR(255) NOT NULL COMMENT '仓库名称',
    address VARCHAR(255) COMMENT '仓库地址',
    status INT DEFAULT 1 COMMENT '状态: 1=启用 0=禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_warehouse_code (warehouse_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='仓库表';
