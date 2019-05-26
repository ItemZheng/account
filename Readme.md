#### 股票交易系统——资金账户子模块

##### 工程环境

+ Java 1.8
+ Intellij Idea
+ 安装插件 lombok（注解）
+ 安装maven（首次打开工程需要等待一段时间，解决环境依赖）

##### 文件说明

`main/resources/application.properties` :工程项目配置，包括数据库地址

##### 文件夹说明

`annotation` :自定义注解，目前有 @LoginIgnore，加了该注解则不进行登陆检查

`config` :spring boot 配置类

`controller` : MVC的C，主要解决的是控制不同请求路径的Manager

`dal`: data access level, 和数据库的交互，这里采用的是 JpaRepository，用函数名字代表对应的数据库操作

`domain`: 数据实体，用注解可以指定对应的数据库

`dto`: data transfer object，定义数据传输格式，随便定义

`interceptor`: 拦截器，在进入controller之前调用拦截器，目前主要实现了登陆拦截器

`service`: 业务逻辑，供controller调用

`util`: 公共类

##### 提交说明

提交逻辑之前需要单元测试，并将单元测试代码上传，对自己的代码负责

附：查看代码主人的方法
https://www.jianshu.com/p/52459a41999b?utm_campaign

##### 目前登陆可用参数

+ username: itemzheng
+ key: 5aa8e18b049dad51660be7b75d9f0fb2
+ timestamp: 1