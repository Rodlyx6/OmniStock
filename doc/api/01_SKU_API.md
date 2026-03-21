# SKU管理接口文档

## 1.1 创建商品

**请求方法：** `POST`

**请求路径：** `/api/v1/skus`

**请求头：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "skuCode": "SKU-001",
  "skuName": "iPhone 15 Pro",
  "category": "电子产品",
  "weight": 0.5,
  "volume": 0.001,
  "abcCategory": 1
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| skuCode | String | 是 | 商品编码，唯一 |
| skuName | String | 是 | 商品名称 |
| category | String | 否 | 商品分类 |
| weight | Decimal | 否 | 重量(kg) |
| volume | Decimal | 否 | 体积(m³) |
| abcCategory | Integer | 否 | ABC分类: 1=A类 2=B类 3=C类 |

**成功响应（201 Created）：**
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "skuId": 1,
    "skuCode": "SKU-001",
    "skuName": "iPhone 15 Pro",
    "category": "电子产品",
    "weight": 0.5,
    "volume": 0.001,
    "abcCategory": 1,
    "version": 0,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

**错误响应（400）：**
```json
{
  "code": 400,
  "message": "商品编码已存在",
  "data": null,
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 1.2 查询商品列表

**请求方法：** `GET`

**请求路径：** `/api/v1/skus`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| category | String | 否 | 商品分类 |
| skuCode | String | 否 | 商品编码 |
| skuName | String | 否 | 商品名称 |

**请求示例：**
```
GET /api/v1/skus?page=1&pageSize=10&category=电子产品
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
        "skuId": 1,
        "skuCode": "SKU-001",
        "skuName": "iPhone 15 Pro",
        "category": "电子产品",
        "weight": 0.5,
        "volume": 0.001,
        "abcCategory": 1,
        "version": 0,
        "createdAt": "2026-03-21T10:30:00Z",
        "updatedAt": "2026-03-21T10:30:00Z"
      }
    ]
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 1.3 查询商品详情

**请求方法：** `GET`

**请求路径：** `/api/v1/skus/{skuId}`

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
    "category": "电子产品",
    "weight": 0.5,
    "volume": 0.001,
    "abcCategory": 1,
    "version": 0,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 1.4 更新商品

**请求方法：** `PUT`

**请求路径：** `/api/v1/skus/{skuId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| skuId | Long | 是 | 商品ID |

**请求体：**
```json
{
  "skuName": "iPhone 15 Pro Max",
  "category": "电子产品",
  "weight": 0.6,
  "volume": 0.0012,
  "abcCategory": 1
}
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "skuId": 1,
    "skuCode": "SKU-001",
    "skuName": "iPhone 15 Pro Max",
    "category": "电子产品",
    "weight": 0.6,
    "volume": 0.0012,
    "abcCategory": 1,
    "version": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:35:00Z"
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```

---

## 1.5 删除商品

**请求方法：** `DELETE`

**请求路径：** `/api/v1/skus/{skuId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| skuId | Long | 是 | 商品ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": "2026-03-21T10:30:00Z"
}
```

**错误响应（400）：**
```json
{
  "code": 400,
  "message": "商品已有库存，无法删除",
  "data": null,
  "timestamp": "2026-03-21T10:30:00Z"
}
```
