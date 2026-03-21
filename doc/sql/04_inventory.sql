-- ============================================================
-- INVENTORY表（库存表 - 核心）
-- ============================================================

CREATE TABLE inventory (
    inventory_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    sku_id BIGINT NOT NULL COMMENT '商品ID',
    location_id BIGINT NOT NULL COMMENT '库位ID',
    quantity INT NOT NULL DEFAULT 0 COMMENT '总库存数量',
    reserved_quantity INT NOT NULL DEFAULT 0 COMMENT '预留数量(已分配未出库)',
    available_quantity INT NOT NULL DEFAULT 0 COMMENT '可用数量(总数-预留)',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号(乐观锁 - 防超卖)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_sku_location (sku_id, location_id),
    INDEX idx_sku_id (sku_id),
    INDEX idx_location_id (location_id),
    INDEX idx_available_quantity (available_quantity),
    CONSTRAINT fk_inventory_sku FOREIGN KEY (sku_id) REFERENCES sku(sku_id),
    CONSTRAINT fk_inventory_location FOREIGN KEY (location_id) REFERENCES location(location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';
