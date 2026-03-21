-- ============================================================
-- LOCATION表（库位表）
-- ============================================================

CREATE TABLE location (
    location_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库位ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    location_code VARCHAR(64) NOT NULL UNIQUE COMMENT '库位编码(如: A-01-01-01)',
    area VARCHAR(32) COMMENT '库区(如: A区)',
    shelf VARCHAR(32) COMMENT '货架号(如: 01)',
    layer INT COMMENT '层数(如: 01)',
    position INT COMMENT '位置(如: 01)',
    capacity INT COMMENT '库位容量(件)',
    current_quantity INT DEFAULT 0 COMMENT '当前库存数量',
    status INT DEFAULT 1 COMMENT '状态: 1=可用 0=禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_location_code (location_code),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_area (area),
    CONSTRAINT fk_location_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库位表';
