# 供应商管理接口文档

## 6.1 创建供应商

**请求方法：** `POST`

**请求路径：** `/api/v1/suppliers`

**请求头：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "supplierCode": "SUP-001",
  "supplierName": "供应商A",
  "contactPerson": "李四",
  "phone": "13800138000",
  "address": "深圳市南山区"
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| supplierCode | String | 是 | 供应商编码，唯一 |
| supplierName | String | 是 | 供应商名称 |
| contactPerson | String | 否 | 联系人 |
| phone | String | 否 | 联系电话 |
| address | String | 否 | 地址 |

**成功响应（201 Created）：**
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "supplierId": 1,
    "supplierCode": "SUP-001",
    "supplierName": "供应商A",
    "contactPerson": "李四",
    "phone": "13800138000",
    "address": "深圳市南山区",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 6.2 查询供应商列表

**请求方法：** `GET`

**请求路径：** `/api/v1/suppliers`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| supplierCode | String | 否 | 供应商编码 |
| supplierName | String | 否 | 供应商名称 |
| status | Integer | 否 | 状态: 1=启用 0=禁用 |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 20,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      {
        "supplierId": 1,
        "supplierCode": "SUP-001",
        "supplierName": "供应商A",
        "contactPerson": "李四",
        "phone": "13800138000",
        "address": "深圳市南山区",
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

## 6.3 查询供应商详情

**请求方法：** `GET`

**请求路径：** `/api/v1/suppliers/{supplierId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| supplierId | Long | 是 | 供应商ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "supplierId": 1,
    "supplierCode": "SUP-001",
    "supplierName": "供应商A",
    "contactPerson": "李四",
    "phone": "13800138000",
    "address": "深圳市南山区",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 6.4 更新供应商

**请求方法：** `PUT`

**请求路径：** `/api/v1/suppliers/{supplierId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| supplierId | Long | 是 | 供应商ID |

**请求体：**
```json
{
  "supplierName": "供应商A升级版",
  "contactPerson": "王五",
  "phone": "13900139000",
  "address": "深圳市福田区",
  "status": 1
}
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "supplierId": 1,
    "supplierCode": "SUP-001",
    "supplierName": "供应商A升级版",
    "contactPerson": "王五",
    "phone": "13900139000",
    "address": "深圳市福田区",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:35:00Z"
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```

---

## 6.5 删除供应商

**请求方法：** `DELETE`

**请求路径：** `/api/v1/suppliers/{supplierId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| supplierId | Long | 是 | 供应商ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

# 员工管理接口文档

## 7.1 创建员工

**请求方法：** `POST`

**请求路径：** `/api/v1/employees`

**请求头：**
```
Content-Type: application/json
Authorization: Bearer {token}
```

**请求体：**
```json
{
  "employeeCode": "EMP-001",
  "employeeName": "张三",
  "email": "zhangsan@example.com",
  "phone": "13700137000"
}
```

**参数说明：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| employeeCode | String | 是 | 员工编码，唯一 |
| employeeName | String | 是 | 员工名称 |
| email | String | 否 | 邮箱 |
| phone | String | 否 | 电话 |

**成功响应（201 Created）：**
```json
{
  "code": 201,
  "message": "创建成功",
  "data": {
    "employeeId": 1,
    "employeeCode": "EMP-001",
    "employeeName": "张三",
    "email": "zhangsan@example.com",
    "phone": "13700137000",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 7.2 查询员工列表

**请求方法：** `GET`

**请求路径：** `/api/v1/employees`

**查询参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| employeeCode | String | 否 | 员工编码 |
| employeeName | String | 否 | 员工名称 |
| status | Integer | 否 | 状态: 1=启用 0=禁用 |

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
        "employeeId": 1,
        "employeeCode": "EMP-001",
        "employeeName": "张三",
        "email": "zhangsan@example.com",
        "phone": "13700137000",
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

## 7.3 查询员工详情

**请求方法：** `GET`

**请求路径：** `/api/v1/employees/{employeeId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| employeeId | Long | 是 | 员工ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "employeeId": 1,
    "employeeCode": "EMP-001",
    "employeeName": "张三",
    "email": "zhangsan@example.com",
    "phone": "13700137000",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:30:00Z"
  },
  "timestamp": "2026-03-21T10:30:00Z"
}
```

---

## 7.4 更新员工

**请求方法：** `PUT`

**请求路径：** `/api/v1/employees/{employeeId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| employeeId | Long | 是 | 员工ID |

**请求体：**
```json
{
  "employeeName": "张三（升级）",
  "email": "zhangsan.new@example.com",
  "phone": "13700137001",
  "status": 1
}
```

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "employeeId": 1,
    "employeeCode": "EMP-001",
    "employeeName": "张三（升级）",
    "email": "zhangsan.new@example.com",
    "phone": "13700137001",
    "status": 1,
    "createdAt": "2026-03-21T10:30:00Z",
    "updatedAt": "2026-03-21T10:35:00Z"
  },
  "timestamp": "2026-03-21T10:35:00Z"
}
```

---

## 7.5 删除员工

**请求方法：** `DELETE`

**请求路径：** `/api/v1/employees/{employeeId}`

**路径参数：**
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| employeeId | Long | 是 | 员工ID |

**成功响应（200）：**
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": "2026-03-21T10:30:00Z"
}
```
