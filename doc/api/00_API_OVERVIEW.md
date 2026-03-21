# API 接口文档总览

## 系统概述

OmniStock 新零售智能仓储管理系统 RESTful API 文档。

**API基础URL：** `http://localhost:8080/api/v1`

**认证方式：** JWT Token（Bearer Token）

**请求头示例：**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

---

## 接口分类

### 1. SKU管理模块
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 创建商品 | POST | `/skus` | 新增商品 |
| 查询商品列表 | GET | `/skus` | 分页查询商品 |
| 查询商品详情 | GET | `/skus/{skuId}` | 获取单个商品信息 |
| 更新商品 | PUT | `/skus/{skuId}` | 修改商品信息 |
| 删除商品 | DELETE | `/skus/{skuId}` | 删除商品 |

**详见：** `01_SKU_API.md`

---

### 2. 仓库管理模块
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 创建仓库 | POST | `/warehouses` | 新增仓库 |
| 查询仓库列表 | GET | `/warehouses` | 分页查询仓库 |
| 查询仓库详情 | GET | `/warehouses/{warehouseId}` | 获取单个仓库信息 |
| 创建库位 | POST | `/warehouses/{warehouseId}/locations` | 新增库位 |
| 查询库位列表 | GET | `/warehouses/{warehouseId}/locations` | 分页查询库位 |
| 查询库位详情 | GET | `/locations/{locationId}` | 获取单个库位信息 |
| 更新库位 | PUT | `/locations/{locationId}` | 修改库位信息 |

**详见：** `02_WAREHOUSE_API.md`

---

### 3. 库存核心模块（重点）
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询库存列表 | GET | `/inventories` | 分页查询库存 |
| 查询库存详情 | GET | `/inventories/{inventoryId}` | 获取单个库存信息 |
| 查询SKU总库存 | GET | `/inventories/sku/{skuId}/total` | 跨库位查询总库存 |
| **库存扣减** | **POST** | **/inventories/deduct** | **防超卖核心接口** |
| 库存增加 | POST | `/inventories/increase` | 入库增加库存 |
| 查询库存流水 | GET | `/inventories/flows` | 分页查询流水记录 |
| 查询流水详情 | GET | `/inventories/flows/{flowId}` | 获取单条流水记录 |

**详见：** `03_INVENTORY_API.md`

---

### 4. 入库管理模块
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 创建入库单 | POST | `/inbounds` | 新增入库单 |
| 查询入库单列表 | GET | `/inbounds` | 分页查询入库单 |
| 查询入库单详情 | GET | `/inbounds/{inboundId}` | 获取单个入库单信息 |
| 确认入库 | POST | `/inbounds/{inboundId}/receive` | 收货确认 |
| 取消入库单 | DELETE | `/inbounds/{inboundId}` | 取消入库单 |

**详见：** `04_INBOUND_API.md`

---

### 5. 出库管理模块
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 创建出库单 | POST | `/outbounds` | 新增出库单 |
| 查询出库单列表 | GET | `/outbounds` | 分页查询出库单 |
| 查询出库单详情 | GET | `/outbounds/{outboundId}` | 获取单个出库单信息 |
| 确认出库 | POST | `/outbounds/{outboundId}/pick` | 拣货完成 |
| 取消出库单 | DELETE | `/outbounds/{outboundId}` | 取消出库单 |
| 部分出库 | POST | `/outbounds/{outboundId}/partial-pick` | 部分拣货 |

**详见：** `05_OUTBOUND_API.md`

---

### 6. 供应商管理模块
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 创建供应商 | POST | `/suppliers` | 新增供应商 |
| 查询供应商列表 | GET | `/suppliers` | 分页查询供应商 |
| 查询供应商详情 | GET | `/suppliers/{supplierId}` | 获取单个供应商信息 |
| 更新供应商 | PUT | `/suppliers/{supplierId}` | 修改供应商信息 |
| 删除供应商 | DELETE | `/suppliers/{supplierId}` | 删除供应商 |

**详见：** `06_SUPPLIER_EMPLOYEE_API.md`

---

### 7. 员工管理模块
| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 创建员工 | POST | `/employees` | 新增员工 |
| 查询员工列表 | GET | `/employees` | 分页查询员工 |
| 查询员工详情 | GET | `/employees/{employeeId}` | 获取单个员工信息 |
| 更新员工 | PUT | `/employees/{employeeId}` | 修改员工信息 |
| 删除员工 | DELETE | `/employees/{employeeId}` | 删除员工 |

**详见：** `06_SUPPLIER_EMPLOYEE_API.md`

---

## 统一响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 具体数据
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

### 分页响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      // 数据列表
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

### 错误响应

```json
{
  "code": 400,
  "message": "错误信息描述",
  "data": null,
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 常用HTTP状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 201 | 创建成功 |
| 400 | 请求参数错误或业务逻辑错误 |
| 401 | 未授权（Token过期或无效） |
| 403 | 禁止访问（权限不足） |
| 404 | 资源不存在 |
| 409 | 冲突（如库存版本冲突） |
| 500 | 服务器内部错误 |

---

## 认证流程

### 1. 登录获取Token

```
POST /api/v1/auth/login

请求体：
{
  "username": "admin",
  "password": "password123"
}

响应：
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 3600
  }
}
```

### 2. 使用Token请求API

```
GET /api/v1/inventories
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 分页参数说明

所有列表查询接口都支持分页，参数如下：

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| page | Integer | 1 | 页码，从1开始 |
| pageSize | Integer | 10 | 每页数量，最大100 |

**示例：**
```
GET /api/v1/inventories?page=2&pageSize=20
```

---

## 日期时间格式

- **日期格式：** `yyyy-MM-dd`（如：2026-03-21）
- **时间戳格式：** ISO 8601（如：2026-03-21T10:30:00Z）

**查询示例：**
```
GET /api/v1/inventories/flows?startDate=2026-03-01&endDate=2026-03-31
```

---

## 核心业务流程

### 入库流程

```
1. 创建入库单
   POST /api/v1/inbounds
   
2. 确认入库（收货）
   POST /api/v1/inbounds/{inboundId}/receive
   
3. 库存自动增加
   库存表 + 库存流水表
   
4. 查询库存流水
   GET /api/v1/inventories/flows?referenceNo=IN-xxx
```

### 出库流程

```
1. 创建出库单
   POST /api/v1/outbounds
   （自动检查库存是否充足）
   
2. 确认出库（拣货）
   POST /api/v1/outbounds/{outboundId}/pick
   
3. 库存自动扣减
   库存表 + 库存流水表
   
4. 查询库存流水
   GET /api/v1/inventories/flows?referenceNo=OUT-xxx
```

### 库存查询流程

```
1. 查询单个库位库存
   GET /api/v1/inventories/{inventoryId}
   
2. 查询SKU总库存（跨库位）
   GET /api/v1/inventories/sku/{skuId}/total
   
3. 查询库存流水（追溯）
   GET /api/v1/inventories/flows?skuId=1&startDate=2026-03-01
```

---

## 重要提示

### 库存扣减防超卖

库存扣减接口采用**混合锁机制**：

1. **第一步：乐观锁**（99%成功）
   - 使用version字段
   - 无阻塞，高性能

2. **第二步：Redis分布式锁**（冲突重试）
   - 获取分布式锁
   - 重新查询库存
   - 再次尝试乐观锁

3. **第三步：返回失败**
   - 如果还是失败，返回库存不足

**错误码：**
- `400`：库存不足
- `409`：版本冲突（自动重试）

### 库存流水追溯

所有库存变化都会记录到 `inventory_flow` 表：

- **INBOUND**：入库
- **OUTBOUND**：出库
- **ADJUST**：调整
- **INVENTORY_CHECK**：盘点

支持按以下维度追溯：
- 按商品ID
- 按库位ID
- 按时间范围
- 按操作人
- 按关联单号

---

## 开发建议

### 前端调用建议

1. **库存查询**
   - 优先使用缓存
   - 定期刷新（5分钟）

2. **库存扣减**
   - 409冲突时自动重试（最多3次）
   - 显示友好的错误提示

3. **流水查询**
   - 支持导出Excel
   - 支持按日期范围查询

### 后端实现建议

1. **缓存策略**
   - 热点商品库存放Redis
   - TTL设置为5分钟

2. **异步处理**
   - 库存流水记录异步写入
   - 使用消息队列解耦

3. **监控告警**
   - 监控库存扣减失败率
   - 监控版本冲突频率
   - 监控Redis锁超时

---

## 联系方式

如有问题，请联系技术支持。
