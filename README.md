
## Easy File Upload 轻量级文件存储系统
![image](http://myfile.buildworld.cn/esfu_logo.png)

> 此项目是后端是基于SpringBoot2.x，前端是基于layui，存储仓库是基于七牛云，开箱即用，可以上传本地和云端文件，支持MD5文件校检，防止文件重复上传。前端文件放入Nginx服务器即可，后端项目可以打包位war包，部署到tomcat服务器下即可，完全前后端分离。你可以当做图床，也可以当做网盘，存储文件。

#### 一、测试环境
[官网地址-www.ai2art.com/fileupload](http://www.ai2art.com/fileupload/)
```
前端：Nginx-1.12.0
后端：Apache-tomcat-8.5.29
数据库：Mysql
```
![image](http://myfile.buildworld.cn/360%E6%88%AA%E5%9B%BE16751025435782.jpg)


---

#### 二、用户使用
##### 1、单个文件上传
> 点击上传或者拖拽文件实现上传，上传完成，自动回调显示文件地址

![image](http://myfile.buildworld.cn/360截图16520817898671.jpg)
![image](http://myfile.buildworld.cn/360截图1793072184105113.jpg)
> 服务器支持文件MD5校检，实现文件查重。点击上传按钮可以实现云端文件上传，即用户只要提供文件的网络地址，无需下载实现云端存储

![image](http://myfile.buildworld.cn/360截图174012037168101.jpg)

##### 2、多个文件上传
> 多文件上传，包含了本地文件查重，上传，秒传，上传失败重传，删除功能。

![image](http://myfile.buildworld.cn/360截图17860604528594.jpg)

##### 3、文件列表
> 文件列表包括文件分页显示，文件搜索，显示文件名称，文件大小，文件MD5，文件网络地址，文件上传时间。暂时不支持删除！开发者可以自己加。

![image](http://myfile.buildworld.cn/360截图17571116264764.jpg)

---

#### 三、开发者
##### 1、后端
> 后端采用SpringBoot+SpringDataJpa框架开发，采用目前最新的SpringBoot稳定版，测试运行正常，部署到Tomcat8+版本即可。数据库名称qiniuyun。

```
 SpringBoot: 2.1.2.RELEASE
```
- 修改application.yml文件配置

```
#七牛云配置，自己到七牛云控制台获取
qiniuyun:
  config:
    accesskey: XXXXXXXXXXXXXXXXXX
    secretkey: XXXXXXXXXXXXXXXXXXXX
    bucket: 七牛云存储空间名称
    fileurl: 文件地址前缀（如 http://myfile.buildworld.cn/）
```
##### 2、前端
> 前端使用layui框架，使用时注意修改请求接口的地址即可
