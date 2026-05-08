CREATE TABLE IF NOT EXISTS warehouse (
    id BIGINT PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    current_usage INT NOT NULL DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sku (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    category VARCHAR(64),
    unit VARCHAR(32),
    weight DOUBLE,
    volume DOUBLE,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS location (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    code VARCHAR(64) NOT NULL,
    max_capacity INT NOT NULL,
    max_weight DOUBLE NOT NULL,
    current_capacity INT DEFAULT 0,
    current_weight DOUBLE DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_warehouse_code (warehouse_id, code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    version INT NOT NULL DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_sku_location (sku_id, location_id),
    KEY idx_location (location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inventory_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sku_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    change_qty INT NOT NULL,
    change_type VARCHAR(32) NOT NULL,
    biz_id VARCHAR(64) NOT NULL,
    operator_name VARCHAR(64) NOT NULL,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_sku (sku_id),
    KEY idx_change_type_time (change_type, created_time),
    KEY idx_biz_id (biz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inbound_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    biz_id VARCHAR(64) NOT NULL,
    sku_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    operator_name VARCHAR(64) NOT NULL,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_biz_id (biz_id),
    KEY idx_sku (sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,
    phone VARCHAR(32),
    avatar VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0:禁用, 1:启用',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE,
    code VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始化默认数据
-- 密码默认为 123456 (加密后的)
-- 管理员角色
INSERT IGNORE INTO sys_role (id, name, code, description) VALUES (1, '超级管理员', 'ROLE_ADMIN', '拥有系统所有权限');
INSERT IGNORE INTO sys_role (id, name, code, description) VALUES (2, '普通用户', 'ROLE_USER', '普通业务操作权限');


INSERT IGNORE INTO sys_user (id, username, password, email, status) VALUES (1, 'admin', '$2a$10$vG/y4Y2QyU.uO5v3X0UeZ.H9y.8gE7oXmR1vW/j8p9r9.oO/Z/Zq.', 'admin@omnistock.com', 1);

-- 分配管理员角色
INSERT IGNORE INTO sys_user_role (id, user_id, role_id) VALUES (1, 1, 1);

CREATE TABLE IF NOT EXISTS outbound_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    biz_id VARCHAR(64) NOT NULL,
    sku_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    operator_name VARCHAR(64) NOT NULL,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_biz_id (biz_id),
    KEY idx_sku (sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
