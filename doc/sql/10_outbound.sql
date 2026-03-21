-- ============================================================
-- OUTBOUND表（出库单表）
-- ============================================================

CREATE TABLE outbound (
    outbound_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '出库单ID',
    outbound_no VARCHAR(64) NOT NULL UNIQUE COMMENT '出库单号',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    status VARCHAR(32) DEFAULT 'PENDING' COMMENT '状态: PENDING(待出库) PICKING(拣货中) COMPLETED(已完成) CANCELLED(已取消)',
    total_quantity INT DEFAULT 0 COMMENT '总数量',
    operator_id BIGINT COMMENT '操作人ID',
    outbound_time TIMESTAMP COMMENT '出库时间',
    remark VARCHAR(255) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_outbound_no (outbound_no),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    CONSTRAINT fk_outbound_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='出库单表';
