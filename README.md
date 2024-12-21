# bmr_final_projet

### set-up :
```
python3 -m venv myTidyVEnv
source myTidyVEnv/bin/activate
pip3 install -r requirements.txt
```

### run server :
```
cd mySearchEngine
python3 manage.py runserver
```

已有功能：
http://127.0.0.1:8000/
books & book/<id>
frenchbooks & frenchbook/<id>
englishbooks & englishbook/<id>

模块介绍：
myGutenburg/

models.py : 数据库模型
更改后需要上传cache新模型结构
```
python3 manage.py makemigrations mytig
python3 manage.py migrate
```

serializer.py : 切片，只选用部分需要的内容。

urls.py : 链接逻辑图

views.py : 网页逻辑，可以后期加前端

