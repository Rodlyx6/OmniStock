-- ============================================================
-- OUTBOUND_ITEM表（出库明细表）
-- ============================================================

CREATE TABLE outbound_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    outbound_id BIGINT NOT NULL COMMENT '出库单ID',
    sku_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '计划出库数量',
    picked_quantity INT DEFAULT 0 COMMENT '实际拣货数量',
    status VARCHAR(32) DEFAULT 'PENDING' COMMENT '状态: PENDING(待拣) PICKED(已拣) PARTIAL(部分拣)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_outbound_id (outbound_id),
    INDEX idx_sku_id (sku_id),
    CONSTRAINT fk_outbound_item_outbound FOREIGN KEY (outbound_id) REFERENCES outbound(outbound_id),
    CONSTRAINT fk_outbound_item_sku FOREIGN KEY (sku_id) REFERENCES sku(sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='出库明细表';
