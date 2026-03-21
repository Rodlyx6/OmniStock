-- ============================================================
-- INBOUND表（入库单表）
-- ============================================================

CREATE TABLE inbound (
    inbound_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '入库单ID',
    inbound_no VARCHAR(64) NOT NULL UNIQUE COMMENT '入库单号',
    supplier_id BIGINT COMMENT '供应商ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    status VARCHAR(32) DEFAULT 'PENDING' COMMENT '状态: PENDING(待入库) RECEIVING(收货中) COMPLETED(已完成) CANCELLED(已取消)',
    total_quantity INT DEFAULT 0 COMMENT '总数量',
    operator_id BIGINT COMMENT '操作人ID',
    inbound_time TIMESTAMP COMMENT '入库时间',
    remark VARCHAR(255) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_inbound_no (inbound_no),
    INDEX idx_supplier_id (supplier_id),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    CONSTRAINT fk_inbound_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id),
    CONSTRAINT fk_inbound_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入库单表';
