-- ============================================================
-- INBOUND_ITEM表（入库明细表）
-- ============================================================

CREATE TABLE inbound_item (
    item_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    inbound_id BIGINT NOT NULL COMMENT '入库单ID',
    sku_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '计划入库数量',
    received_quantity INT DEFAULT 0 COMMENT '实际收货数量',
    status VARCHAR(32) DEFAULT 'PENDING' COMMENT '状态: PENDING(待收) RECEIVED(已收) PARTIAL(部分收)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_inbound_id (inbound_id),
    INDEX idx_sku_id (sku_id),
    CONSTRAINT fk_inbound_item_inbound FOREIGN KEY (inbound_id) REFERENCES inbound(inbound_id),
    CONSTRAINT fk_inbound_item_sku FOREIGN KEY (sku_id) REFERENCES sku(sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入库明细表';
