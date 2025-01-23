# java--version--单体版

## user部分 
项目后管部分，主要负责用户增删改查 

### 目前状态 :
- [x] 用户部分修改完成，可以正常使用
- [x] remote部分完成
- [x] 收藏功能待完成

## project部分 
项目主体，负责完成老师要求的内容

### 目前状态 :
- [x] 根据书名，作者，语言，类型搜索 (基于sql)
- [x] 根据词搜索 （基于elasticsearch）
- [x] 根据短语/短句搜索（不能有标点符号）（基于elasticsearch）
- [x] 根据正则表达式搜索（基于elasticsearch）
- [x] 排序已完成，基于点击数
- [x] 推荐初步已完成，
- [x] 推荐修改为jaccard版本
- [x] kmp实现文件内搜索
- [x] egrep实现文件内搜索

### elasticsearch插入图书方法
#### 1.在数据库中插入书籍时将在storage_path插入本地的图书位置（理论上远程地址也行 但是我没试）
#### 2.将es_sync_flag置为0（代表未同步进elasticsearch）
#### 3.定时任务将每五分钟（间隔在yaml文件中可以修改）扫描一次库，将未同步的数据同步到elasticsearch中
#### 4.有时候会跳出来 “链接已关闭” exception 这是正常现象，不影响使用
#### 5.本地图书馆位置在resources/book

### 数据库插入方法
#### 1.直接在数据库中插入
#### 2.使用文件
##### 2.1 将想插入的内容写入ressource/bookInfoFormwork.csv
##### 2.2 把bookInfoFormwork.csv移动到ressource/bookInfo
##### 2.3 启动程序
##### 2.4 然后就没有然后了，程序会自己读取bookInfoFormwork.csv，然后删除，并备份到ressource/bookInfoBakcingup中，然后在ressource下新建一个空白的（只有表头的）bookInfoFormwork.csv
    


## 注意事项
### 记得改配置文件！！！！！！
#### mysql 账户名 密码 库名 端口
#### redis 密码 端口
#### elasticsearch 端口 账号 密码

### 记得加载resources里的sql文件
### 记得把resources里的elasticsearch索引加载到elasticsearch里（有俩，记得分别执行）
