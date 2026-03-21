-- ============================================================
-- SKU表（商品主表）
-- ============================================================

CREATE TABLE sku (
    sku_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    sku_code VARCHAR(64) NOT NULL UNIQUE COMMENT '商品编码',
    sku_name VARCHAR(255) NOT NULL COMMENT '商品名称',
    category VARCHAR(64) COMMENT '商品分类',
    weight DECIMAL(10, 2) COMMENT '重量(kg)',
    volume DECIMAL(10, 2) COMMENT '体积(m³)',
    abc_category INT COMMENT 'ABC分类: 1=A类 2=B类 3=C类',
    version INT DEFAULT 0 COMMENT '版本号(乐观锁)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_sku_code (sku_code),
    INDEX idx_category (category),
    INDEX idx_abc_category (abc_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';
