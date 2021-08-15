# 运行
命令行输入mvnw spring-boot:run

# 访问
默认为localhost:8080

# 接口
### 登录接口
/login
### 方法类型
Post
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| userName | String | RequestBody | 用户名，非空 |
| password | String | RequestBody | 密码，非空 |
### 返回值
null


### 注册接口
/register
### 方法类型
Post
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| userName | String | RequestBody | 用户名，非空 |
| password | String | RequestBody | 密码，非空 |
### 返回值
token:String


### 实体列表接口
/getInstanceList
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| course | String | Param | 学科，非空 |
| searchKey | String | Param | 关键字，非空 |
| offset | Integer | Param | 偏移量，缺省值0 |
| limit | Integer | Param | 获取个数，缺省值50 |
| token | String | Param | token |
### 返回值
列表:List


### 实体详情接口
/getInfoByInstanceName
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| course | String | Param | 学科，可选 |
| name | String | Param | 实体名，非空 |
| token | String | Param | token |
### 返回值
详细信息:JSONObject


### 问答接口
/inputQuestion
### 方法类型
Post
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| course | String | RequestBody | 学科，可选 |
| inputQuestion | String | RequestBody | 问题，非空 |
| token | String | RequestBody | token |
### 返回值
回答:JSONObject
