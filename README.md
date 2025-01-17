# java--version--单体版

## user部分 
项目后管部分，主要负责用户增删改查 

### 目前状态 :
- [x] 用户部分修改完成，可以正常使用（但是token验证貌似有点小问题，不知道问题出在哪，不过问题不大，反正项目用不上）
- [ ] remote部分待完成
- [ ] 进一步去掉无用部分

原有短链接部分没删（万一用上了呢hhhh）
分组部分没删（可以拿来用做书籍收藏的分组）


## project部分 
项目主体，负责完成老师要求的内容

### 目前状态 :
- [x] 根据书名，作者，语言，类型搜索 (基于sql)
- [x] 根据词搜索 （基于elasticsearch）
- [x] 根据短语/短句搜索（不能有标点符号）（基于elasticsearch）
- [x] 根据正则表达式搜索（基于elasticsearch）
- [ ] 排序
- [ ] 推荐

### 待完成
都待完成（完成之后记得完成user部分的remote）

## 注意事项
### 记得改配置文件！！！！！！
#### mysql 账户名 密码 库名 端口
#### redis 密码 端口
#### elasticsearch 端口 账号 密码

### 记得加载resources里的sql文件
### 记得把resources里的elasticsearch索引加载到elasticsearch里
