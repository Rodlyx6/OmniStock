# OmniStock 后端快速开始

## 项目结构

```
backend/
├── src/main/java/com/omnistock/backend/
│   ├── constant/              # 常量
│   │   ├── BusinessConstant   # 业务常量
│   │   └── ErrorCode          # 错误码
│   ├── common/                # 通用类
│   │   ├── Result             # 统一返回结果
│   │   ├── BusinessException  # 业务异常
│   │   └── GlobalExceptionHandler  # 全局异常处理
│   ├── config/                # 配置类
│   │   ├── MybatisPlusConfig  # MyBatis Plus配置
│   │   └── SwaggerConfig      # Swagger配置
│   ├── domain/                # 领域模型
│   │   ├── entity/            # 数据库实体
│   │   ├── dto/               # 数据传输对象
│   │   └── vo/                # 视图对象
│   ├── mapper/                # 数据访问层
│   ├── service/               # 业务逻辑层
│   │   ├── InventoryService   # 库存服务接口
│   │   ├── OutboundService    # 出库服务接口
│   │   └── impl/              # 实现类
│   ├── controller/            # 控制层
│   │   ├── InventoryController
│   │   └── OutboundController
│   └── BackendApplication.java
└── resources/
    └── application.yml        # 应用配置
```

## 核心功能

### 1. 库存扣减（防超卖）
- **接口**: `POST /v1/inventories/deduct`
- **机制**: 混合锁（乐观锁 + Redis分布式锁）
- **流程**:
  1. 查询库存
  2. 检查库存是否充足
  3. 尝试乐观锁扣减
  4. 失败则使用Redis分布式锁重试
  5. 记录库存流水

### 2. 出库管理
- **创建出库单**: `POST /v1/outbounds`
  - 检查库存充足
  - 创建出库单和明细
  
- **确认出库**: `POST /v1/outbounds/{outboundId}/pick`
  - 扣减库存
  - 记录流水
  - 更新出库单状态

### 3. 库存查询
- **查询库存列表**: `GET /v1/inventories`
- **查询库存详情**: `GET /v1/inventories/{inventoryId}`
- **查询SKU总库存**: `GET /v1/inventories/sku/{skuId}/total`

## 快速启动

### 1. 数据库初始化

```bash
# 按顺序执行SQL脚本
mysql -u root -p omnistock < doc/sql/01_sku.sql
mysql -u root -p omnistock < doc/sql/02_warehouse.sql
mysql -u root -p omnistock < doc/sql/03_location.sql
mysql -u root -p omnistock < doc/sql/04_inventory.sql
mysql -u root -p omnistock < doc/sql/05_inventory_flow.sql
mysql -u root -p omnistock < doc/sql/08_outbound.sql
mysql -u root -p omnistock < doc/sql/11_outbound_item.sql
```

### 2. 启动应用

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 3. 访问API文档

```
http://localhost:8080/api/swagger-ui.html
```

## 配置说明

### application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/omnistock
    username: root
    password: 88888888
  data:
    redis:
      host: localhost
      port: 6379
      password: 123321
```

## 核心设计

### 1. 分层架构
- **Controller**: 请求入口，参数校验
- **Service**: 业务逻辑，事务管理
- **Mapper**: 数据访问，SQL执行
- **Entity**: 数据库映射
- **DTO**: 请求数据传输
- **VO**: 响应数据展示

### 2. 防超卖机制

```
库存扣减流程：
1. 查询库存 + 版本号
2. 检查库存是否充足
3. 乐观锁更新：UPDATE ... WHERE version = ?
   ├─ 成功 → 记录流水，返回成功
   └─ 失败 → 进入第4步
4. Redis分布式锁重试
   ├─ 获取锁成功 → 重新查询 → 再次尝试乐观锁
   ├─ 获取锁失败 → 返回版本冲突
   └─ 锁超时 → 返回系统错误
```

### 3. 库存流水记录

每一次库存变化都记录：
- INBOUND: 入库
- OUTBOUND: 出库
- ADJUST: 调整
- INVENTORY_CHECK: 盘点

支持完整追溯：
- 按SKU查询
- 按时间范围查询
- 按操作人查询
- 按关联单号查询

## 错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 201 | 创建成功 |
| 5001 | 库存不足 |
| 5002 | 库存版本冲突 |
| 7002 | 出库单不存在 |
| 7003 | 出库单状态错误 |
| 7008 | 库存不足，无法创建出库单 |
| 500 | 系统错误 |

## 测试示例

### 1. 创建出库单

```bash
curl -X POST http://localhost:8080/api/v1/outbounds \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "operatorId": 1,
    "remark": "订单出库",
    "items": [
      {
        "skuId": 1,
        "quantity": 100
      }
    ]
  }'
```

### 2. 确认出库

```bash
curl -X POST http://localhost:8080/api/v1/outbounds/1/pick \
  -H "Content-Type: application/json" \
  -d '{
    "operatorId": 1,
    "items": [
      {
        "itemId": 1,
        "pickedQuantity": 100
      }
    ]
  }'
```

### 3. 查询库存

```bash
curl http://localhost:8080/api/v1/inventories?page=1&pageSize=10
```

## 下一步

- [ ] 实现入库管理
- [ ] 实现库存流水查询
- [ ] 实现库存盘点
- [ ] 实现库存调拨
- [ ] 添加缓存优化
- [ ] 添加性能测试

## 常见问题

### Q: 库存扣减失败怎么办？
A: 检查错误码：
- 5001: 库存不足，需要补货
- 5002: 版本冲突，自动重试
- 500: 系统错误，检查日志

### Q: 如何追溯库存变化？
A: 使用库存流水查询接口（待实现）

### Q: 如何处理并发扣减？
A: 系统已实现混合锁机制，自动处理并发冲突

## 联系方式

如有问题，请查看接口文档：`doc/api/`
