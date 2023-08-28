### 1.文件代码一键生成

基于MyBatis-Plus快速生成MVC架构骨架，包含controller-->service-->serviceImpl-->dao-->mapper->pojo

根据个人编码习惯,生成的基本文件骨架，由于使用了MyBatis-Plus,简单的单表完全由其生成，因此在这里并没有像其他一键生成代码一样,生成基本的crud代码和sql.xml，完全由使用者根据业务场景定义方法名称。

![image-20230828110453598](https://edu-guli-0418.oss-cn-beijing.aliyuncs.com/uPic/image-20230828110453598.png)

![image-20230724142455273](https://edu-guli-0418.oss-cn-beijing.aliyuncs.com/uPic/image-20230724142455273.png)

![image-20230724143111902](https://edu-guli-0418.oss-cn-beijing.aliyuncs.com/uPic/image-20230724143111902.png)



### 2.根据实体类，快速生成建表SQL

如果存在MyBatis-Plus的@TableName注解 则使用其value作为表名称；支持大部分的数据类型，如不支持，会用?代替。此外还需要使用者介入，简单修改Varchar的字符长度，索引等。

<img src="https://edu-guli-0418.oss-cn-beijing.aliyuncs.com/uPic/image-20230724143339025.png" alt="image-20230724143339025" style="zoom:50%;" />

![image-20230724143604797](https://edu-guli-0418.oss-cn-beijing.aliyuncs.com/uPic/image-20230724143604797.png)
