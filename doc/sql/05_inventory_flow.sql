-- ============================================================
-- INVENTORY_FLOW表（库存流水表 - 核心）
-- ============================================================

CREATE TABLE inventory_flow (
    flow_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流水ID',
    sku_id BIGINT NOT NULL COMMENT '商品ID',
    inventory_id BIGINT NOT NULL COMMENT '库存ID',
    flow_type VARCHAR(32) NOT NULL COMMENT '流水类型: INBOUND(入库) OUTBOUND(出库) ADJUST(调整) INVENTORY_CHECK(盘点)',
    quantity_change INT NOT NULL COMMENT '数量变化(可正可负)',
    before_quantity INT NOT NULL COMMENT '变化前数量',
    after_quantity INT NOT NULL COMMENT '变化后数量',
    reference_no VARCHAR(64) COMMENT '关联单号(入库单/出库单)',
    operator_id BIGINT COMMENT '操作人ID',
    remark VARCHAR(255) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_sku_id (sku_id),
    INDEX idx_inventory_id (inventory_id),
    INDEX idx_flow_type (flow_type),
    INDEX idx_reference_no (reference_no),
    INDEX idx_created_at (created_at),
    INDEX idx_operator_id (operator_id),
    CONSTRAINT fk_flow_sku FOREIGN KEY (sku_id) REFERENCES sku(sku_id),
    CONSTRAINT fk_flow_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(inventory_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存流水表 - 完整记录所有库存变化';
