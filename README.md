# 版本
1.0.2

# 运行
命令行输入mvnw spring-boot:run

# 访问
默认为localhost:8080

# 接口

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
null


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
token:String


### 登出接口
/logout
### 方法类型
Put
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| token | String | RequestBody | token |
### 返回值
null


### 修改密码接口
/modifyPassword
### 方法类型
Put
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| oldPassword | String | RequestBody | 原密码，非空 |
| newPassword | String | RequestBody | 新密码，非空 |
| token | String | RequestBody | token |
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
| label | String | Param | 筛选标签，默认不筛选 |
| sorted | Boolean | Param | 是否按相关度排序，默认不排序 |
| token | String | Param | token |
### 返回值
列表:List
### 返回样例
```json
[
    {
        "relevancy": 22,
        "label": "初唐四杰",
        "category": "人物"
    },
    {
        "relevancy": 19,
        "label": "唐宋八大家",
        "category": "人物"
    }
]
```


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
```json
{
    "property": [
        {
            "predicateLabel": "气候类型",
            "object": "以热带气候为主，热带草原气候面积广大。东非高原的赤道附近，由于海拔高，气温低，没有形成热带雨林气候。"
        },
        {
            "predicateLabel": "纬度",
            "object": "主要在南、北回归线之间，赤道穿过中部，绝大部分位于热带，非洲是典型的“热带大陆”"
        },
        {
            "predicateLabel": "特征",
            "object": "世界上人口自然增长率最高"
        },
        ...
        {
            "predicateLabel": "特征",
            "object": "最炎热的大陆，也是最炎热的大洲"
        },
        {
            "predicateLabel": "学术论文",
            "object": "4131FD21"
        }
    ],
    "label": "非洲",
    "content": [
        {
            "subject_label": "亚洲与非洲",
            "predicate_label": "强相关于"
        },
        {
            "subject_label": "圣多美和普林西比",
            "predicate_label": "位于"
        },
        {
            "predicate_label": "实体限制",
            "object_label": "粮食问题"
        },
        ...
        {
            "subject_label": "加纳",
            "predicate_label": "位于"
        }
    ]
}
```


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


### 知识链接接口
/linkInstance
### 方法类型
Post
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| context | String | RequestBody | 待识别文本，非空 |
| course | String | RequestBody | 学科，可选 |
| token | String | RequestBody | token |
### 返回值
知识标注:JSONObject


### 实体相关习题列表接口
/getQuestionListByUriName
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| uriName | String | Param | 实体名，非空 |
| offset | Integer | Param | 偏移量，缺省值0 |
| limit | Integer | Param | 获取个数，缺省值10 |
| token | String | Param | token |
### 返回值
习题列表:List
### 返回样例
```json
[
    {
        "qAnswer": "C",
        "A": "黑色人种-非洲北部",
        "B": "白色人种-非洲中部",
        "C": "黄色人种-亚洲东部",
        "D": "混血人种-欧洲",
        "id": 183924,
        "qBody": "下列人种与其主要分布地区的组合,正确的是()"
    },
    {
        "qAnswer": "B",
        "A": "大洋洲、北冰洋",
        "B": "亚洲、太平洋",
        "C": "非洲、大西洋",
        "D": "北美洲、印度洋",
        "id": 183767,
        "qBody": "面积最大的大洲和面积最大的大洋分别是:()"
    },
    {
        "qAnswer": "A",
        "A": "环太平洋地带",
        "B": "亚洲内部",
        "C": "北冰洋沿岸",
        "D": "非洲",
        "id": 183710,
        "qBody": "世界上火山、地震最频繁的地区分布在()"
    }
]
```


### 实体访问历史记录接口
/getInstanceHistory
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| token | String | Param | token |
### 返回值
String[]
### 返回样例
```json
[
    "李白",
    "杜甫"
]
```


### 清除实体访问历史记录接口
/clearInstanceHistory
### 方法类型
Put
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| token | String | RequestBody | token |
### 返回值
null


### 收藏列表接口
/getFavourite
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| token | String | Param | token |
### 返回值
收藏列表:String[]
### 返回样例
```json
[
    "李白"
]
```


### 设置收藏接口
/setFavourite
### 方法类型
PUT
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| name | String | RequestBody | 实体名 |
| token | String | RequestBody | token |
### 返回值
null


### 取消收藏接口
/resetFavourite
### 方法类型
PUT
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| name | String | RequestBody | 实体名 |
| token | String | RequestBody | token |
### 返回值
null


### 搜索历史记录接口
/getSearchHistory
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| token | String | Param | token |
### 返回值
搜索历史记录:ImmutablePair<String, String>[]
### 返回样例
```json
[
    {
        "chinese": "李"
    },
    {
        "chinese": "杜"
    },
    {
        "chinese": "阿房宫"
    }
]
```


### 清除搜索历史记录接口
/clearSearchHistory
### 方法类型
Put
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| token | String | RequestBody | token |
### 返回值
null


### 热门标签接口
/getHotLabel
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| subject | String | Param | 学科 |
| token | String | Param | token |
### 返回值
热门标签:String[]
### 返回样例
```json
[
    "人物",
    "作品",
    "作文",
    "修辞手法",
    "古代诗歌",
    "句子",
    "句式",
    "字形",
    "字音",
    "对联"
]
```