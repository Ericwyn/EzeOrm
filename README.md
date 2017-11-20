# 简介
EzeOrm 是一个使用java 编写的简易ORM（Object Relational Mapping 对象关系映射）框架。 关于ORM框架的简介请参考  [对象关系映射——维基百科](https://zh.wikipedia.org/wiki/%E5%AF%B9%E8%B1%A1%E5%85%B3%E7%B3%BB%E6%98%A0%E5%B0%84) 。帮助编写者更加简单的将自己的java 程序与数据库相连接。

## 数据库支持
 - mysql
 - ~~SQLite~~ 尚未支持

## 项目依赖
 - [ConfigGet工具](https://github.com/Ericwyn/JavaUtil/blob/master/src/ConfigGet/README.md)
  
# 使用
    EzeDBBuilder.EzeDB userServer=new EzeDBBuilder()
                .url("aaa")
                .setClass(User.class)
                .build();
    System.out.println(userServer);

# 对象关系映射表
### **mysql**里的映射
    
|    java 类型    | mysql 类型 |
| :--: | :--: |
| int、Integer、byte、Byte、short、Short、long、Long、Boolean、boolean |  INT |
| Long       |  BIGINT |
| float、Float、double、Double       |  DOUBLE |
| Date       |  DATE |
| char Char String       |  TEXT |

## 主要注解说明
### `@Entity`
 - 使用在类当中，标记实体类
 - `table` 属性设定表的名字
### `@Column`
 - 使用在类的变量当中，代表该类是数据表中的一个字段
 - `name` 设定字段名
 - `type` 设定字段类型  （只能从ColumnType 中选择）
 - `notNull` 设定字段是否可为空 （默认为`false`,代表可为空）
### `@AutoIncrement`
 - 使用在类变量当中，代表该字段是自动增加的
### `PrimaryKey`
 - 使用在类变量当中，代表该字段是主键
 

## 配置文件`ezeorm.cfg` 说明
配置文件的读取和使用，基于ConfigGet工具

    db_connect_url =jdbc:mysql://localhost:3306/${db_name}?characterEncoding=utf-8&useUnicode=true
    db_account = root 
    db_password = password 


# 主要架构备注
 - 通过`EzeDbServerBuilder` 创建EzeDbServer,每一个实体类对象，绑定一个EzeDbServer ，而后再绑定一张表
 - 所有的`EzeDbServer` 共同使用`EzeSql`类，`EzeSql`是`EzeOrm`最底层的jdbc封装，所有的构建好的sql语句，直接在EzeSql类中执行。
 - 所以`EzeSql`类中，有所有类共用的`Connection` 和`Statement`，以及sql语句执行方法。
 - 各个实体类对象绑定的`EzeDbServer`，都要通过`MySQLCodeBuilder` 来生成增删改查使用的SQL语句，而后再传输到`EzeSql`的sql执行方法当中

  