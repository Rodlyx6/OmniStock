# 仓库管理接口文档

## 2.1 创建仓库

**请求方法：** `POST`

**请求路径：** `/api/v1/warehouses`

**请求头：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "warehouseCode": "WH-001",
  "warehouseName": "北京中心仓",
  "address": "北京市朝阳区"
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| warehouseCode | String | 是 | 仓库编码，唯一 |
| warehouseName | String | 是 | 仓库名称 |
| address | String | 否 | 仓库地址 |

**成功响应（201 Created）：**
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "warehouseId": 1,
    "warehouseCode": "WH-001",
    "warehouseName": "北京中心仓",
    "address": "北京市朝阳区",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 2.2 查询仓库列表

**请求方法：** `GET`

**请求路径：** `/api/v1/warehouses`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| warehouseCode | String | 否 | 仓库编码 |
| warehouseName | String | 否 | 仓库名称 |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 5,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      {
        "warehouseId": 1,
        "warehouseCode": "WH-001",
        "warehouseName": "北京中心仓",
        "address": "北京市朝阳区",
        "status": 1,
        "createdAt": "2026-03-21T10:30:00Z",
        "updatedAt": "2026-03-21T10:30:00Z"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 2.3 查询仓库详情

**请求方法：** `GET`

**请求路径：** `/api/v1/warehouses/{warehouseId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| warehouseId | Long | 是 | 仓库ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "warehouseId": 1,
    "warehouseCode": "WH-001",
    "warehouseName": "北京中心仓",
    "address": "北京市朝阳区",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 2.4 创建库位

**请求方法：** `POST`

**请求路径：** `/api/v1/warehouses/{warehouseId}/locations`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| warehouseId | Long | 是 | 仓库ID |

**请求体：**
```json
{
  "locationCode": "A-01-01-01",
  "area": "A区",
  "shelf": "01",
  "layer": 1,
  "position": 1,
  "capacity": 1000
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| locationCode | String | 是 | 库位编码，唯一，格式: 区-货架-层-位置 |
| area | String | 否 | 库区 |
| shelf | String | 否 | 货架号 |
| layer | Integer | 否 | 层数 |
| position | Integer | 否 | 位置 |
| capacity | Integer | 否 | 库位容量(件) |

**成功响应（201 Created）：**
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "locationId": 1,
    "warehouseId": 1,
    "locationCode": "A-01-01-01",
    "area": "A区",
    "shelf": "01",
    "layer": 1,
    "position": 1,
    "capacity": 1000,
    "currentQuantity": 0,
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 2.5 查询库位列表

**请求方法：** `GET`

**请求路径：** `/api/v1/warehouses/{warehouseId}/locations`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| warehouseId | Long | 是 | 仓库ID |

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认20 |
| area | String | 否 | 库区 |
| locationCode | String | 否 | 库位编码 |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 20,
    "list": [
      {
        "locationId": 1,
        "warehouseId": 1,
        "locationCode": "A-01-01-01",
        "area": "A区",
        "shelf": "01",
        "layer": 1,
        "position": 1,
        "capacity": 1000,
        "currentQuantity": 500,
        "status": 1,
        "createdAt": "2026-03-21T10:30:00Z",
        "updatedAt": "2026-03-21T10:30:00Z"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 2.6 查询库位详情

**请求方法：** `GET`

**请求路径：** `/api/v1/locations/{locationId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| locationId | Long | 是 | 库位ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "locationId": 1,
    "warehouseId": 1,
    "locationCode": "A-01-01-01",
    "area": "A区",
    "shelf": "01",
    "layer": 1,
    "position": 1,
    "capacity": 1000,
    "currentQuantity": 500,
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 2.7 更新库位

**请求方法：** `PUT`

**请求路径：** `/api/v1/locations/{locationId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| locationId | Long | 是 | 库位ID |

**请求体：**
```json
{
  "area": "A区",
  "shelf": "01",
  "layer": 1,
  "position": 1,
  "capacity": 1200,
  "status": 1
}
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "locationId": 1,
    "warehouseId": 1,
    "locationCode": "A-01-01-01",
    "area": "A区",
    "shelf": "01",
    "layer": 1,
    "position": 1,
    "capacity": 1200,
    "currentQuantity": 500,
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:35:00Z"
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```
