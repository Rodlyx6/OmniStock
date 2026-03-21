# 入库管理接口文档

## 4.1 创建入库单

**请求方法：** `POST`

**请求路径：** `/api/v1/inbounds`

**请求头：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "supplierId": 1,
  "warehouseId": 1,
  "operatorId": 1,
  "remark": "采购订单PO-001",
  "items": [
    {
      "skuId": 1,
      "quantity": 500
    },
    {
      "skuId": 2,
      "quantity": 300
    }
  ]
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| supplierId | Long | 否 | 供应商ID |
| warehouseId | Long | 是 | 仓库ID |
| operatorId | Long | 是 | 操作人ID |
| remark | String | 否 | 备注 |
| items | Array | 是 | 入库明细 |
| items[].skuId | Long | 是 | 商品ID |
| items[].quantity | Integer | 是 | 计划入库数量 |

**成功响应（201 Created）：**
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "inboundId": 1,
    "inboundNo": "IN-20260321-001",
    "supplierId": 1,
    "supplierName": "供应商A",
    "warehouseId": 1,
    "warehouseName": "北京中心仓",
    "status": "PENDING",
    "totalQuantity": 800,
    "operatorId": 1,
    "operatorName": "张三",
    "remark": "采购订单PO-001",
    "inboundTime": null,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z",
    "items": [
      {
        "itemId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "quantity": 500,
        "receivedQuantity": 0,
        "status": "PENDING"
      },
      {
        "itemId": 2,
        "skuId": 2,
        "skuCode": "SKU-002",
        "skuName": "iPhone 15",
        "quantity": 300,
        "receivedQuantity": 0,
        "status": "PENDING"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 4.2 查询入库单列表

**请求方法：** `GET`

**请求路径：** `/api/v1/inbounds`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| status | String | 否 | 状态: PENDING, RECEIVING, COMPLETED, CANCELLED |
| warehouseId | Long | 否 | 仓库ID |
| supplierId | Long | 否 | 供应商ID |
| inboundNo | String | 否 | 入库单号 |
| startDate | String | 否 | 开始日期(yyyy-MM-dd) |
| endDate | String | 否 | 结束日期(yyyy-MM-dd) |

**请求示例：**
```
GET /api/v1/inbounds?status=PENDING&warehouseId=1&page=1&pageSize=10
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
        "inboundId": 1,
        "inboundNo": "IN-20260321-001",
        "supplierId": 1,
        "supplierName": "供应商A",
        "warehouseId": 1,
        "warehouseName": "北京中心仓",
        "status": "PENDING",
        "totalQuantity": 800,
        "operatorId": 1,
        "operatorName": "张三",
        "remark": "采购订单PO-001",
        "inboundTime": null,
        "createdAt": "2026-03-21T10:30:00Z",
        "updatedAt": "2026-03-21T10:30:00Z"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 4.3 查询入库单详情

**请求方法：** `GET`

**请求路径：** `/api/v1/inbounds/{inboundId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| inboundId | Long | 是 | 入库单ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "inboundId": 1,
    "inboundNo": "IN-20260321-001",
    "supplierId": 1,
    "supplierName": "供应商A",
    "warehouseId": 1,
    "warehouseName": "北京中心仓",
    "status": "PENDING",
    "totalQuantity": 800,
    "operatorId": 1,
    "operatorName": "张三",
    "remark": "采购订单PO-001",
    "inboundTime": null,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z",
    "items": [
      {
        "itemId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "quantity": 500,
        "receivedQuantity": 0,
        "status": "PENDING"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 4.4 确认入库（收货）

**请求方法：** `POST`

**请求路径：** `/api/v1/inbounds/{inboundId}/receive`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| inboundId | Long | 是 | 入库单ID |

**请求体：**
```json
{
  "items": [
    {
      "itemId": 1,
      "receivedQuantity": 500,
      "locationId": 1
    },
    {
      "itemId": 2,
      "receivedQuantity": 300,
      "locationId": 2
    }
  ],
  "operatorId": 1
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| items | Array | 是 | 收货明细 |
| items[].itemId | Long | 是 | 入库明细ID |
| items[].receivedQuantity | Integer | 是 | 实际收货数量 |
| items[].locationId | Long | 是 | 库位ID |
| operatorId | Long | 是 | 操作人ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "入库成功",
  "data": {
    "inboundId": 1,
    "inboundNo": "IN-20260321-001",
    "status": "COMPLETED",
    "totalQuantity": 800,
    "inboundTime": "2026-03-21T10:35:00Z",
    "items": [
      {
        "itemId": 1,
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "quantity": 500,
        "receivedQuantity": 500,
        "status": "RECEIVED"
      }
    ],
    "flows": [
      {
        "flowId": 1000,
        "skuId": 1,
        "flowType": "INBOUND",
        "quantityChange": 500,
        "beforeQuantity": 0,
        "afterQuantity": 500,
        "referenceNo": "IN-20260321-001"
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
  "message": "收货数量不能超过计划数量",
  "data": {
    "itemId": 1,
    "plannedQuantity": 500,
    "receivedQuantity": 600
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```

---

## 4.5 取消入库单

**请求方法：** `DELETE`

**请求路径：** `/api/v1/inbounds/{inboundId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| inboundId | Long | 是 | 入库单ID |

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
    "inboundId": 1,
    "inboundNo": "IN-20260321-001",
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
  "message": "已完成的入库单无法取消",
  "data": null,
  "timestamp": "2026-03-21T10:40:00Z"
}
```
