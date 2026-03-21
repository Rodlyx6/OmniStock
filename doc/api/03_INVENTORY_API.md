# 库存核心接口文档

## 3.1 查询库存列表

**请求方法：** `GET`

**请求路径：** `/api/v1/inventories`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| skuId | Long | 否 | 商品ID |
| warehouseId | Long | 否 | 仓库ID |
| locationId | Long | 否 | 库位ID |

**请求示例：**
```
GET /api/v1/inventories?skuId=1&warehouseId=1&page=1&pageSize=10
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 50,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      {
        "inventoryId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "locationId": 1,
        "locationCode": "A-01-01-01",
        "quantity": 1000,
        "reservedQuantity": 200,
        "availableQuantity": 800,
        "version": 5,
        "createdAt": "2026-03-21T10:30:00Z",
        "updatedAt": "2026-03-21T10:35:00Z"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 3.2 查询库存详情

**请求方法：** `GET`

**请求路径：** `/api/v1/inventories/{inventoryId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| inventoryId | Long | 是 | 库存ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "inventoryId": 1,
    "skuId": 1,
    "skuCode": "SKU-001",
    "skuName": "iPhone 15 Pro",
    "locationId": 1,
    "locationCode": "A-01-01-01",
    "quantity": 1000,
    "reservedQuantity": 200,
    "availableQuantity": 800,
    "version": 5,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:35:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 3.3 查询SKU总库存（跨库位）

**请求方法：** `GET`

**请求路径：** `/api/v1/inventories/sku/{skuId}/total`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| skuId | Long | 是 | 商品ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "skuId": 1,
    "skuCode": "SKU-001",
    "skuName": "iPhone 15 Pro",
    "totalQuantity": 5000,
    "totalReservedQuantity": 1000,
    "totalAvailableQuantity": 4000,
    "locations": [
      {
        "locationId": 1,
        "locationCode": "A-01-01-01",
        "quantity": 1000,
        "reservedQuantity": 200,
        "availableQuantity": 800
      },
      {
        "locationId": 2,
        "locationCode": "A-01-01-02",
        "quantity": 2000,
        "reservedQuantity": 300,
        "availableQuantity": 1700
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 3.4 库存扣减（防超卖 - 核心接口）

**请求方法：** `POST`

**请求路径：** `/api/v1/inventories/deduct`

**请求头：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "skuId": 1,
  "quantity": 100,
  "referenceNo": "OUT-20260321-001",
  "operatorId": 1,
  "remark": "订单出库"
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| skuId | Long | 是 | 商品ID |
| quantity | Integer | 是 | 扣减数量 |
| referenceNo | String | 是 | 关联单号（出库单号） |
| operatorId | Long | 是 | 操作人ID |
| remark | String | 否 | 备注 |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "扣减成功",
  "data": {
    "inventoryId": 1,
    "skuId": 1,
    "skuCode": "SKU-001",
    "skuName": "iPhone 15 Pro",
    "beforeQuantity": 1000,
    "afterQuantity": 900,
    "deductQuantity": 100,
    "version": 6,
    "flowId": 1001,
    "createdAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

**错误响应（400 - 库存不足）：**
```json
{
  "code": 400,
  "message": "库存不足，可用库存: 50",
  "data": {
    "availableQuantity": 50,
    "requestQuantity": 100
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

**错误响应（409 - 版本冲突）：**
```json
{
  "code": 409,
  "message": "库存版本冲突，请重试",
  "data": null,
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 3.5 库存增加（入库）

**请求方法：** `POST`

**请求路径：** `/api/v1/inventories/increase`

**请求体：**
```json
{
  "skuId": 1,
  "locationId": 1,
  "quantity": 500,
  "referenceNo": "IN-20260321-001",
  "operatorId": 1,
  "remark": "采购入库"
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| skuId | Long | 是 | 商品ID |
| locationId | Long | 是 | 库位ID |
| quantity | Integer | 是 | 增加数量 |
| referenceNo | String | 是 | 关联单号（入库单号） |
| operatorId | Long | 是 | 操作人ID |
| remark | String | 否 | 备注 |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "增加成功",
  "data": {
    "inventoryId": 1,
    "skuId": 1,
    "skuCode": "SKU-001",
    "skuName": "iPhone 15 Pro",
    "beforeQuantity": 1000,
    "afterQuantity": 1500,
    "increaseQuantity": 500,
    "version": 7,
    "flowId": 1002,
    "createdAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 3.6 查询库存流水

**请求方法：** `GET`

**请求路径：** `/api/v1/inventories/flows`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认20 |
| skuId | Long | 否 | 商品ID |
| inventoryId | Long | 否 | 库存ID |
| flowType | String | 否 | 流水类型: INBOUND, OUTBOUND, ADJUST, INVENTORY_CHECK |
| referenceNo | String | 否 | 关联单号 |
| operatorId | Long | 否 | 操作人ID |
| startDate | String | 否 | 开始日期(yyyy-MM-dd) |
| endDate | String | 否 | 结束日期(yyyy-MM-dd) |

**请求示例：**
```
GET /api/v1/inventories/flows?skuId=1&flowType=OUTBOUND&page=1&pageSize=20
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 1000,
    "pageNum": 1,
    "pageSize": 20,
    "list": [
      {
        "flowId": 1001,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "inventoryId": 1,
        "flowType": "OUTBOUND",
        "quantityChange": -100,
        "beforeQuantity": 1000,
        "afterQuantity": 900,
        "referenceNo": "OUT-20260321-001",
        "operatorId": 1,
        "operatorName": "张三",
        "remark": "订单出库",
        "createdAt": "2026-03-21T10:30:00Z"
      },
      {
        "flowId": 1000,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "inventoryId": 1,
        "flowType": "INBOUND",
        "quantityChange": 500,
        "beforeQuantity": 500,
        "afterQuantity": 1000,
        "referenceNo": "IN-20260321-001",
        "operatorId": 1,
        "operatorName": "张三",
        "remark": "采购入库",
        "createdAt": "2026-03-21T09:30:00Z"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 3.7 查询库存流水详情

**请求方法：** `GET`

**请求路径：** `/api/v1/inventories/flows/{flowId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| flowId | Long | 是 | 流水ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "flowId": 1001,
    "skuId": 1,
    "skuCode": "SKU-001",
    "skuName": "iPhone 15 Pro",
    "inventoryId": 1,
    "flowType": "OUTBOUND",
    "quantityChange": -100,
    "beforeQuantity": 1000,
    "afterQuantity": 900,
    "referenceNo": "OUT-20260321-001",
    "operatorId": 1,
    "operatorName": "张三",
    "remark": "订单出库",
    "createdAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```
