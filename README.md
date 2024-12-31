# bmr_final_projet
**新年快乐** ：）
更新于出去旅游当天 233333333


### 项目构想 :

Web/mobile application <br>
先写完web端简单的，然后弄mobile

##### 目标：
1. Explicit feature “Search” 
2. Explicitfeature“Advancedsearch”
3. Implicit feature of ranking
4. Implicit feature of recommendation

3: 去匹配数据元数据，比如：标题，作者，xxx （目前元数据就搞了id, 标题, 语言, 作者, 出版时间，反正还可以加。。。）。返回的查询列表搞一种匹配度算法？根据匹配到关键词的数据之类的，按照匹配度进行排序。比如标题和作者的权重会非常大。具体算法没想好
4: 根据搜索记录来尝试提取用户喜欢的方向/作者，推荐10个？前四个最感兴趣的方向分别推荐4，3，2，1个（根据最热门的->最近下载数量）

##### 前端：
欢迎界面 -> login + register + search 按钮 
 -> 点击后跳转登录/注册 
 -> 完成后在返回到欢迎界面
 -> 登录后刷新推荐列表，没有搜索记录就选择原网站当前最受欢迎的10本书。
 -> search 输入后跳转到搜索界面。返回一个匹配的list
 -> 点击后显示书本简介以及read online按钮，然后点击后显示原文

### set-up :
```
python3 -m venv myTidyVEnv
source myTidyVEnv/bin/activate
pip3 install -r requirements.txt
```
### preparation :
```
python3 manage.py crawlBooks 1 100
```
预加载数据
数字代表id范围

### run server :
```
cd mySearchEngine
python3 manage.py runserver
```

已有功能：
```
http://127.0.0.1:8000/
books & book/<id>
frenchbooks & frenchbook/<id>
englishbooks & englishbook/<id>
```

```
login : 

register :
```
登录和注册界面，直接写的post方法！

填入数据使用的是json格式，{"username":"name", "password":"pwd"}

很麻烦，但是没写前端是这样的 :(

目前将每次搜索与用户关联起来了，相当于搜索记录。可能后期只保留前50条或者70

模块介绍：
myGutenburg/

models.py : 数据库模型
更改后需要上传cache新模型结构
```
python3 manage.py makemigrations mygutenberg
python3 manage.py migrate
```

serializer.py : 切片，只选用部分需要的内容。

urls.py : 链接逻辑图

views.py : 网页逻辑，可以后期加前端

