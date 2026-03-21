-- ============================================================
-- EMPLOYEE表（员工表）
-- ============================================================

CREATE TABLE employee (
    employee_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '员工ID',
    employee_code VARCHAR(64) NOT NULL UNIQUE COMMENT '员工编码',
    employee_name VARCHAR(64) NOT NULL COMMENT '员工名称',
    email VARCHAR(128) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    status INT DEFAULT 1 COMMENT '状态: 1=启用 0=禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_employee_code (employee_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';
