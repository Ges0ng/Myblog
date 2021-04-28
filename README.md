# MyBlog

#### 介绍
前端 thymeleaf + semantic-ui + ajax </br></br>
后端 springboot2.0 + spring data JPA + mysql 5.7 + druid连接池监控数据 + redis作为二级缓存 + knife4j接口文档(pro环境自动屏蔽)</br>
插件 mail邮件通知 + easy-captcha验证码登录 + oshi监控服务器信息 + quartz定时任务备份数据库（但是java不能直接执行linux的命令，仍旧需要依赖第三方库ganymed）</br>

做成的个人博客项目, 使用 aspects 生成和记录日志内容存入数据库, quartz定时备份数据库.</br>

验证码验证登录后台,也可以用base64来转换存储验证码.</br>
发起请求时，获取真实ip地址并转换为地区信息，</br>
相关功能有兴趣的可以自己研究.</br></br>
由于是个人博客所以不开放注册功能,只有博主 即管理员才能进行登录和相关的管理. 管理员账户用简单的MD5验证，后期会升级成bearer token验证.</br>

博客内容采用的markdown编辑器,可以通过简单、易读易写的文本格式生成结构化的HTML文档。    
具有代码高亮,二维码自主生成,博客目录自主侦测,页面动画等插件供使用,有兴趣的可以自己参考官方文档来添加特效相关.

可以点赞,评论,赞赏,是否转载等,都是可以自主选择开启关闭, 有保存草稿的功能.

博客基本页面: 博客信息,博客内容,分类,标签,归档,音乐盒,关于我以及友链功能(会自动跳转到我自己写的音乐项目).

音乐盒中的歌曲都是接入的网易云的api,并没有后端参与.有兴趣可以自己研究



示范地址:
http://paracosm.top

<br/><br/><br/><br/><br/><br/>

2021.1.26 20:47 

 **1.** 更新了博客首页的图片如果上传分辨率过大会突出导致样式混乱的bug

 **2.** 更新了移动端下标题栏有可能打不开的bug

 **3.** 添加了看板娘
1.更新了博客首页的图片如果上传分辨率过大会突出导致样式混乱的错误

2.更新了移动端下标题栏有可能打不开的bug
3.添加了看板娘

-------------------------------------

2021.1.28

4.修复了图片上传后比例的问题。
5.增加了某些管理员功能

2021.3.20
  增加了少许功能,优化了代码

-------------------------------------

2021.4.28
  </br>
  有一说一，不推荐使用jpa作为ORM框架,毕竟对于负责查询的时候还是不太灵活,还是建议使用mybatis,自己写sql比较好.不过可以学习一下,对于简单查询还是非常好用的，稍微复杂一点的查询是真头疼，难搞喔。
  </br>
  HQLJPQL中用了过时的方法,sql查询还没优化，有时间重写一下或者换成mybatis吧。。。
