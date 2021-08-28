# 版本
1.3.2

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
        "course": "chinese",
        "relevancy": 22,
        "label": "初唐四杰",
        "category": "人物"
    },
    {
        "course": "chinese",
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
| course | String | Param | 学科 |
| name | String | Param | 实体名，非空 |
| token | String | Param | token |
### 返回值
详细信息:JSONObject
```json
{
    "property": [
        {
            "predicateLabel": "人口特征",
            "object": "90%以上为黑种人，被称为“黑种人的故乡”。<br>"
        },
        {
            "predicateLabel": "特征",
            "object": "世界上人口自然增长率最高<br>最炎热的大陆，也是最炎热的大洲<br>"
        },
        {
            "predicateLabel": "地理位置",
            "object": "赤道穿过<br>跨东西两半球<br>"
        },
        {
            "predicateLabel": "别名",
            "object": "贫穷大陆<br>热带大陆<br>富饶大陆<br>高原大陆<br>"
        },
        ...
        {
            "predicateLabel": "措施",
            "object": "控制人口增长，提高农业生产水平，保护生态环境，是撒哈拉以南非洲国家解决人口、粮食、环境问题的重要措施。<br>"
        },
        {
            "predicateLabel": "面积",
            "object": "3000万平方千米<br>"
        }
    ],
    "label": "非洲",
    "isFavourite": true,
    "content": [
        {
            "predicate_label": "穿过",
            "content": [
                {
                    "subject_label": "南回归线",
                    "subject_course": "geo"
                },
                {
                    "subject_label": "北回归线",
                    "subject_course": "geo"
                },
                {
                    "subject_label": "赤道",
                    "subject_course": "geo"
                }
            ]
        },
        ...
        {
            "predicate_label": "强相关于",
            "content": [
                {
                    "subject_label": "亚洲与非洲",
                    "subject_course": "geo"
                }
            ]
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
回答:JSONArray
### 返回样例
```
苏轼字什么？
[
    {
        "all": "",
        "fsanswer": "",
        "subject": "苏轼",
        "message": "",
        "tamplateContent": "(?<title>(.*)?)字(是)?什么(.*)?",
        "fs": 0,
        "filterStr": "",
        "subjectUri": "http://edukb.org/knowledge/0.1/instance/chinese#-19af7078638913cdd1bf94ed98dd9052",
        "predicate": "字",
        "score": 28.284271247461902,
        "answerflag": false,
        "attention": "",
        "fsscore": "",
        "value": "子瞻"
    }
]
```
```json
水调歌头作者是谁？
[
    {
        "all": "",
        "fsanswer": "",
        "subject": "水调歌头",
        "message": "",
        "tamplateContent": "(?<title>(.*)?)作者(.*)?",
        "fs": 0,
        "filterStr": "",
        "subjectUri": "http://edukb.org/knowledge/0.1/instance/chinese#gushiwen_view_60035",
        "predicate": "作者",
        "score": 40,
        "answerflag": false,
        "attention": "",
        "fsscore": "",
        "value": "▲京镗▲侯置▲俞国宝▲冯取洽▲刘一止▲刘克庄▲刘望之▲刘清夫▲刘辰翁▲刘过▲利登▲华岳▲叶梦得▲向滈▲吕渭老▲吴渊▲吴潜▲吴镒▲周紫芝▲夏元鼎▲岳甫▲张元干▲张嗣初▲张孝祥▲张继先▲易祓▲晁端礼▲曹勋▲曾布▲曾觌▲朱敦儒▲朱熹▲李光▲李曾伯▲杨炎正▲林淳▲汪莘▲沈瀛▲王千秋▲王灼▲王罙高▲王质▲甄龙友▲石孝友▲程大昌▲管鉴▲胡寅▲苏 轼▲苏轼▲范成大▲葛立方▲葛胜仲▲葛郯▲葛长庚▲蔡伸▲袁去华▲贺铸▲赵善括▲赵师侠▲赵希蓬▲赵彦端▲辛弃疾▲陈三聘▲陈居仁▲陈敷▲韩元吉▲韩玉▲魏了翁"
    }
]
```
```json
明月几时有作者是谁？
[
    {
        "all": "",
        "fsanswer": "",
        "subject": "苏轼",
        "message": "",
        "tamplateContent": "(?<title>(.*)?)(作者是|是谁写的)(.*)?",
        "fs": 2,
        "filterStr": "明月几时有",
        "subjectUri": "http://edukb.org/knowledge/0.1/instance/chinese#-19af7078638913cdd1bf94ed98dd9052",
        "predicate": "主要作品",
        "score": 4.856241650445832,
        "answerflag": true,
        "attention": "",
        "fsscore": "",
        "value": "《水调歌头》（明月几时有）"
    }
]
```


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
知识标注:List
### 返回样例
```json
"course":""
"context":"水调歌头（明月几时有）是北宋苏轼的词"
[
    {
        "entity_type": "宋词",
        "start_index": 0,
        "end_index": 3,
        "entity_course": "chinese",
        "entity": "水调歌头"
    },
    {
        "entity_type": "作品",
        "start_index": 5,
        "end_index": 6,
        "entity_course": "chinese",
        "entity": "明月"
    },
    {
        "entity_type": "朝代",
        "start_index": 12,
        "end_index": 13,
        "entity_course": "history",
        "entity": "北宋"
    },
    {
        "entity_type": "文化名人",
        "start_index": 14,
        "end_index": 15,
        "entity_course": "history",
        "entity": "苏轼"
    }
]
```


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
实体访问历史记录:ImmutablePair<String, String>[]
### 返回样例
```json
[
    {
        "chinese": "李白"
    },
    {
        "chinese": "杜甫"
    }
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
收藏列表:ImmutablePair<String, String>[]
### 返回样例
```json
[
    {
        "chinese": "李白"
    }
]
```


### 设置收藏接口
/setFavourite
### 方法类型
PUT
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| course | String | RequestBody | 学科 |
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
| course | String | RequestBody | 学科 |
| name | String | RequestBody | 实体名 |
| token | String | RequestBody | token |
### 返回值
null


### 判断收藏接口
/isFavourite
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| course | String | Param | 学科 |
| name | String | Param | 实体名 |
| token | String | Param | token |
### 返回值
是否收藏:Boolean
### 返回样例
```json
true
```


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


### 热门实体接口
/getHotInstance
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
    "马克思主义哲学",
    "中国共产党",
    "人民代表大会制度",
    "价值观",
    "科教兴国",
    "改革开放",
    "哲学",
    "世界观",
    "三个代表",
    "综合国力"
]
```


### 推荐题目接口
/getRecommandQuestionList
### 方法类型
Get
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| limit | Integer | Param | 获取个数，缺省值10 |
| token | String | Param | token |
### 返回值
推荐题目:List
### 返回样例
```json
[
    {
        "qAnswer": "A",
        "A": "水稻",
        "B": "小麦",
        "C": "玉米",
        "D": "粟",
        "id": 184755,
        "qBody": "在南宋王朝统治区内,产量居于首位的粮食作物是()"
    },
    {
        "qAnswer": "A",
        "A": "战国到五代",
        "B": "传说中的黄帝到汉武帝",
        "C": "战国到北宋",
        "D": "春秋到五代",
        "id": 184792,
        "qBody": "《资治通鉴》记述的历史时期是()"
    },
    {
        "qAnswer": "A",
        "A": "①②③④",
        "B": "①②④③",
        "C": "①④②③",
        "D": "①③②④",
        "id": 184766,
        "qBody": "下列事件按时间排列,正确的是()①蒙古政权的建立②定国号为元③定都大都④灭亡南宋"
    }
]
```


### 记录答题情况接口
/answerQuestion
### 方法类型
POST
### 参数
| 名称 | 类型 | 位置 | 描述 |
| ---- | ---- | ---- | ---- |
| answer | JSONArray | RequestBody | 作答情况 |
| token | String | RequestBody | token |
### 返回值
null
### 参数样例
```json
{
    "answer":[
        {
            "id":184755,
            "isCorrect":true
        },
        {
            "id":184792,
            "isCorrect":false
        },
        {
            "id":184766,
            "isCorrect":true
        }
    ],
    "token":"ABCDEFG"
}
```