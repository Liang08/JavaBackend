# 运行
命令行输入mvnw spring-boot:run

# 访问
默认为localhost:8080

# 接口
登录接口
/login
方法类型
Post
参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| userName | String | RequestBody | 用户名，非空 |
| password | String | RequestBody | 密码，非空 |

注册接口
/register
方法类型
Post
参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| userName | String | RequestBody | 用户名，非空 |
| password | String | RequestBody | 密码，非空 |
