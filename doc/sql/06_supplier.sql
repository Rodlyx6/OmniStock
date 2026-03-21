-- ============================================================
-- SUPPLIER表（供应商表）
-- ============================================================

CREATE TABLE supplier (
    supplier_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
    supplier_code VARCHAR(64) NOT NULL UNIQUE COMMENT '供应商编码',
    supplier_name VARCHAR(255) NOT NULL COMMENT '供应商名称',
    contact_person VARCHAR(64) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '地址',
    status INT DEFAULT 1 COMMENT '状态: 1=启用 0=禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_supplier_code (supplier_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商表';
