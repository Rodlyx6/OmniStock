# OmniStock 文档中心

## 📚 文档结构

```
doc/
├── sql/                          # 数据库SQL脚本
│   ├── 01_sku.sql               # SKU表
│   ├── 02_warehouse.sql         # 仓库表
│   ├── 03_location.sql          # 库位表
│   ├── 04_inventory.sql         # 库存表（核心）
│   ├── 05_inventory_flow.sql    # 库存流水表（核心）
│   ├── 06_supplier.sql          # 供应商表
│   ├── 07_employee.sql          # 员工表
│   ├── 08_inbound.sql           # 入库单表
│   ├── 09_inbound_item.sql      # 入库明细表
│   ├── 10_outbound.sql          # 出库单表
│   └── 11_outbound_item.sql     # 出库明细表
│
└── api/                          # API接口文档
    ├── 00_API_OVERVIEW.md       # API总览（必读）
    ├── 01_SKU_API.md            # SKU管理接口
    ├── 02_WAREHOUSE_API.md      # 仓库管理接口
    ├── 03_INVENTORY_API.md      # 库存核心接口（重点）
    ├── 04_INBOUND_API.md        # 入库管理接口
    ├── 05_OUTBOUND_API.md       # 出库管理接口
    ├── 06_SUPPLIER_EMPLOYEE_API.md  # 供应商/员工接口
    └── 07_ERROR_CODES.md        # 错误码文档
```

---

## 🗄️ 数据库文档

### SQL脚本说明

所有SQL脚本都在 `doc/sql/` 目录下，按照表的依赖关系编号：

| 文件 | 表名 | 说明 | 优先级 |
|------|------|------|--------|
| 01_sku.sql | sku | 商品主表 | ⭐⭐⭐ |
| 02_warehouse.sql | warehouse | 仓库表 | ⭐⭐⭐ |
| 03_location.sql | location | 库位表 | ⭐⭐⭐ |
| 04_inventory.sql | inventory | 库存表（含乐观锁） | ⭐⭐⭐⭐⭐ |
| 05_inventory_flow.sql | inventory_flow | 库存流水表（完整追溯） | ⭐⭐⭐⭐⭐ |
| 06_supplier.sql | supplier | 供应商表 | ⭐⭐ |
| 07_employee.sql | employee | 员工表 | ⭐⭐ |
| 08_inbound.sql | inbound | 入库单表 | ⭐⭐⭐ |
| 09_inbound_item.sql | inbound_item | 入库明细表 | ⭐⭐⭐ |
| 10_outbound.sql | outbound | 出库单表 | ⭐⭐⭐ |
| 11_outbound_item.sql | outbound_item | 出库明细表 | ⭐⭐⭐ |

### 建表顺序

```bash
# 1. 基础表（无依赖）
mysql> source doc/sql/01_sku.sql;
mysql> source doc/sql/02_warehouse.sql;
mysql> source doc/sql/06_supplier.sql;
mysql> source doc/sql/07_employee.sql;

# 2. 关联表（依赖基础表）
mysql> source doc/sql/03_location.sql;
mysql> source doc/sql/04_inventory.sql;
mysql> source doc/sql/05_inventory_flow.sql;
mysql> source doc/sql/08_inbound.sql;
mysql> source doc/sql/09_inbound_item.sql;
mysql> source doc/sql/10_outbound.sql;
mysql> source doc/sql/11_outbound_item.sql;
```

### 核心表说明

#### inventory表（库存表）
- **version字段**：乐观锁，防超卖
- **available_quantity**：可用库存 = quantity - reserved_quantity
- **唯一索引**：(sku_id, location_id)

#### inventory_flow表（库存流水表）
- **完整记录**：所有库存变化都记录
- **不删除**：只新增，支持完整追溯
- **支持维度**：按SKU、按时间、按操作人、按单号

---

## 📡 API文档

### 快速开始

**必读文档：** `doc/api/00_API_OVERVIEW.md`

包含：
- API基础URL和认证方式
- 接口分类总览
- 统一响应格式
- 常用HTTP状态码
- 核心业务流程

### 模块接口文档

| 文档 | 模块 | 接口数 | 说明 |
|------|------|--------|------|
| 01_SKU_API.md | SKU管理 | 5个 | 商品的增删改查 |
| 02_WAREHOUSE_API.md | 仓库管理 | 7个 | 仓库、库位的增删改查 |
| 03_INVENTORY_API.md | 库存核心 | 7个 | **防超卖、流水追溯** |
| 04_INBOUND_API.md | 入库管理 | 5个 | 入库单的全生命周期 |
| 05_OUTBOUND_API.md | 出库管理 | 6个 | 出库单的全生命周期 |
| 06_SUPPLIER_EMPLOYEE_API.md | 供应商/员工 | 10个 | 基础数据管理 |

### 错误码文档

**文档：** `doc/api/07_ERROR_CODES.md`

包含：
- 错误码分类（1000-9999）
- 错误处理示例
- 常见错误场景
- 最佳实践

---

## 🔑 核心接口

### 库存扣减（防超卖）

```
POST /api/v1/inventories/deduct

防超卖机制：
1. 乐观锁（version字段）
2. Redis分布式锁（冲突重试）
3. 完整流水记录

错误码：
- 5001：库存不足
- 5002：版本冲突（自动重试）
```

**详见：** `doc/api/03_INVENTORY_API.md` - 3.4节

### 库存流水查询（完整追溯）

```
GET /api/v1/inventories/flows

支持维度：
- 按商品ID
- 按库位ID
- 按流水类型（INBOUND/OUTBOUND/ADJUST/INVENTORY_CHECK）
- 按时间范围
- 按操作人
- 按关联单号
```

**详见：** `doc/api/03_INVENTORY_API.md` - 3.6节

---

## 📊 业务流程

### 入库流程

```
1. 创建入库单
   POST /api/v1/inbounds
   
2. 确认入库（收货）
   POST /api/v1/inbounds/{inboundId}/receive
   
3. 自动处理：
   - 库存增加
   - 库存流水记录
   - 库位更新
   
4. 查询追溯
   GET /api/v1/inventories/flows?referenceNo=IN-xxx
```

### 出库流程

```
1. 创建出库单（自动检查库存）
   POST /api/v1/outbounds
   
2. 确认出库（拣货）
   POST /api/v1/outbounds/{outboundId}/pick
   
3. 自动处理：
   - 库存扣减（防超卖）
   - 库存流水记录
   - 库位更新
   
4. 查询追溯
   GET /api/v1/inventories/flows?referenceNo=OUT-xxx
```

### 库存查询流程

```
1. 单个库位库存
   GET /api/v1/inventories/{inventoryId}
   
2. SKU总库存（跨库位）
   GET /api/v1/inventories/sku/{skuId}/total
   
3. 库存流水（完整追溯）
   GET /api/v1/inventories/flows?skuId=1&startDate=2026-03-01
```

---

## 🛠️ 开发指南

### 后端开发

#### 1. 建表

```bash
# 按顺序执行SQL脚本
mysql -u root -p omnistock < doc/sql/01_sku.sql
mysql -u root -p omnistock < doc/sql/02_warehouse.sql
# ... 其他表
```

#### 2. 实现接口

按照 `doc/api/` 中的接口文档实现：

- Controller层：接收请求，参数校验
- Service层：业务逻辑，事务管理
- Mapper层：数据访问，SQL执行

#### 3. 错误处理

参考 `doc/api/07_ERROR_CODES.md`：

- 使用统一的错误码
- 返回统一的错误响应格式
- 记录详细的错误日志

### 前端开发

#### 1. API调用

```javascript
// 查询库存
GET /api/v1/inventories?page=1&pageSize=10

// 库存扣减（处理版本冲突）
POST /api/v1/inventories/deduct
// 如果返回409，自动重试（最多3次）

// 查询流水
GET /api/v1/inventories/flows?skuId=1&startDate=2026-03-01
```

#### 2. 错误处理

```javascript
// 库存不足
if (response.code === 5001) {
  message.error(`库存不足，可用库存: ${response.data.availableQuantity}`);
}

// 版本冲突（自动重试）
if (response.code === 5002) {
  retryCount++;
  if (retryCount < 3) {
    // 重新调用接口
  }
}

// Token过期
if (response.code === 2003) {
  router.push('/login');
}
```

---

## 📋 检查清单

### 数据库检查

- [ ] 所有11个表都已创建
- [ ] 外键关系正确
- [ ] 索引已创建
- [ ] 字符集为utf8mb4

### API检查

- [ ] 所有接口都已实现
- [ ] 参数校验完整
- [ ] 错误码正确
- [ ] 响应格式统一

### 功能检查

- [ ] 库存扣减防超卖
- [ ] 库存流水完整记录
- [ ] 入库流程正常
- [ ] 出库流程正常
- [ ] 库存查询准确

---

## 📞 常见问题

### Q1: 库存扣减失败怎么办？

**A:** 检查错误码：
- 5001：库存不足，需要补货
- 5002：版本冲突，自动重试
- 5008/5009：分布式锁失败，检查Redis

### Q2: 如何追溯库存变化？

**A:** 使用库存流水查询接口：
```
GET /api/v1/inventories/flows?skuId=1&startDate=2026-03-01&endDate=2026-03-31
```

### Q3: 库位编码规则是什么？

**A:** 格式为 `区-货架-层-位置`，例如：
- A-01-01-01：A区 01号货架 第1层 第1个位置
- B-02-03-05：B区 02号货架 第3层 第5个位置

### Q4: 如何处理并发扣减？

**A:** 系统采用混合锁机制：
1. 乐观锁（99%成功）
2. Redis分布式锁（冲突重试）
3. 自动重试最多3次

---

## 🚀 下一步

### STEP 4：后端开发

- [ ] 创建Spring Boot项目
- [ ] 配置MyBatis Plus
- [ ] 实现Entity和Mapper
- [ ] 实现Service和ServiceImpl
- [ ] 实现Controller
- [ ] 配置全局异常处理
- [ ] 添加Swagger文档

### STEP 5：前端开发

- [ ] 创建Vue3项目
- [ ] 配置Tailwind CSS
- [ ] 实现API调用层
- [ ] 实现页面组件
- [ ] 实现状态管理

### STEP 6：联调测试

- [ ] 前后端接口联调
- [ ] 功能测试
- [ ] 性能测试
- [ ] 并发测试

---

## 📖 文档维护

### 更新规则

- 新增接口时，更新对应的API文档
- 新增错误码时，更新错误码文档
- 修改表结构时，更新SQL脚本

### 版本控制

- 所有文档都在Git中管理
- 修改前创建分支
- 修改后提交PR进行审核

---

## 📞 联系方式

如有问题，请联系技术支持。

---

**最后更新：** 2026-03-21

**文档版本：** 1.0

**维护人：** 架构师团队
