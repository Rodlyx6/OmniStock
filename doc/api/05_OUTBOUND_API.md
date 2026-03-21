# 出库管理接口文档

## 5.1 创建出库单

**请求方法：** `POST`

**请求路径：** `/api/v1/outbounds`

**请求头：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "warehouseId": 1,
  "operatorId": 1,
  "remark": "订单号ORD-001",
  "items": [
    {
      "skuId": 1,
      "quantity": 100
    },
    {
      "skuId": 2,
      "quantity": 50
    }
  ]
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| warehouseId | Long | 是 | 仓库ID |
| operatorId | Long | 是 | 操作人ID |
| remark | String | 否 | 备注 |
| items | Array | 是 | 出库明细 |
| items[].skuId | Long | 是 | 商品ID |
| items[].quantity | Integer | 是 | 计划出库数量 |

**成功响应（201 Created）：**
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "outboundId": 1,
    "outboundNo": "OUT-20260321-001",
    "warehouseId": 1,
    "warehouseName": "北京中心仓",
    "status": "PENDING",
    "totalQuantity": 150,
    "operatorId": 1,
    "operatorName": "张三",
    "remark": "订单号ORD-001",
    "outboundTime": null,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z",
    "items": [
      {
        "itemId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "quantity": 100,
        "pickedQuantity": 0,
        "status": "PENDING"
      },
      {
        "itemId": 2,
        "skuId": 2,
        "skuCode": "SKU-002",
        "skuName": "iPhone 15",
        "quantity": 50,
        "pickedQuantity": 0,
        "status": "PENDING"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

**错误响应（400 - 库存不足）：**
```json
{
  "code": 400,
  "message": "库存不足，SKU-001可用库存: 50",
  "data": {
    "skuId": 1,
    "skuCode": "SKU-001",
    "requestQuantity": 100,
    "availableQuantity": 50
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 5.2 查询出库单列表

**请求方法：** `GET`

**请求路径：** `/api/v1/outbounds`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| status | String | 否 | 状态: PENDING, PICKING, COMPLETED, CANCELLED |
| warehouseId | Long | 否 | 仓库ID |
| outboundNo | String | 否 | 出库单号 |
| startDate | String | 否 | 开始日期(yyyy-MM-dd) |
| endDate | String | 否 | 结束日期(yyyy-MM-dd) |

**请求示例：**
```
GET /api/v1/outbounds?status=PENDING&warehouseId=1&page=1&pageSize=10
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      {
        "outboundId": 1,
        "outboundNo": "OUT-20260321-001",
        "warehouseId": 1,
        "warehouseName": "北京中心仓",
        "status": "PENDING",
        "totalQuantity": 150,
        "operatorId": 1,
        "operatorName": "张三",
        "remark": "订单号ORD-001",
        "outboundTime": null,
        "createdAt": "2026-03-21T10:30:00Z",
        "updatedAt": "2026-03-21T10:30:00Z"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 5.3 查询出库单详情

**请求方法：** `GET`

**请求路径：** `/api/v1/outbounds/{outboundId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| outboundId | Long | 是 | 出库单ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "outboundId": 1,
    "outboundNo": "OUT-20260321-001",
    "warehouseId": 1,
    "warehouseName": "北京中心仓",
    "status": "PENDING",
    "totalQuantity": 150,
    "operatorId": 1,
    "operatorName": "张三",
    "remark": "订单号ORD-001",
    "outboundTime": null,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z",
    "items": [
      {
        "itemId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "quantity": 100,
        "pickedQuantity": 0,
        "status": "PENDING"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 5.4 确认出库（拣货完成）

**请求方法：** `POST`

**请求路径：** `/api/v1/outbounds/{outboundId}/pick`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| outboundId | Long | 是 | 出库单ID |

**请求体：**
```json
{
  "items": [
    {
      "itemId": 1,
      "pickedQuantity": 100
    },
    {
      "itemId": 2,
      "pickedQuantity": 50
    }
  ],
  "operatorId": 1
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| items | Array | 是 | 拣货明细 |
| items[].itemId | Long | 是 | 出库明细ID |
| items[].pickedQuantity | Integer | 是 | 实际拣货数量 |
| operatorId | Long | 是 | 操作人ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "出库成功",
  "data": {
    "outboundId": 1,
    "outboundNo": "OUT-20260321-001",
    "status": "COMPLETED",
    "totalQuantity": 150,
    "outboundTime": "2026-03-21T10:35:00Z",
    "items": [
      {
        "itemId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "quantity": 100,
        "pickedQuantity": 100,
        "status": "PICKED"
      }
    ],
    "flows": [
      {
        "flowId": 1001,
        "skuId": 1,
        "flowType": "OUTBOUND",
        "quantityChange": -100,
        "beforeQuantity": 1000,
        "afterQuantity": 900,
        "referenceNo": "OUT-20260321-001"
      }
    ]
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```

**错误响应（400）：**
```json
{
  "code": 400,
  "message": "拣货数量不能超过计划数量",
  "data": {
    "itemId": 1,
    "plannedQuantity": 100,
    "pickedQuantity": 150
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```

---

## 5.5 取消出库单

**请求方法：** `DELETE`

**请求路径：** `/api/v1/outbounds/{outboundId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| outboundId | Long | 是 | 出库单ID |

**请求体：**
```json
{
  "operatorId": 1,
  "remark": "订单取消"
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| operatorId | Long | 是 | 操作人ID |
| remark | String | 否 | 取消原因 |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "取消成功",
  "data": {
    "outboundId": 1,
    "outboundNo": "OUT-20260321-001",
    "status": "CANCELLED",
    "updatedAt": "2026-03-21T10:40:00Z"
  },
  "timestamp": "2026-03-21T10:40:00Z"
}
```

**错误响应（400）：**
```json
{
  "code": 400,
  "message": "已完成的出库单无法取消",
  "data": null,
  "timestamp": "2026-03-21T10:40:00Z"
}
```

---

## 5.6 部分出库

**请求方法：** `POST`

**请求路径：** `/api/v1/outbounds/{outboundId}/partial-pick`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| outboundId | Long | 是 | 出库单ID |

**请求体：**
```json
{
  "items": [
    {
      "itemId": 1,
      "pickedQuantity": 50
    }
  ],
  "operatorId": 1,
  "remark": "部分出库，剩余待出"
}
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "部分出库成功",
  "data": {
    "outboundId": 1,
    "outboundNo": "OUT-20260321-001",
    "status": "PARTIAL",
    "totalQuantity": 150,
    "pickedQuantity": 50,
    "items": [
      {
        "itemId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "quantity": 100,
        "pickedQuantity": 50,
        "status": "PARTIAL"
      }
    ]
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```
