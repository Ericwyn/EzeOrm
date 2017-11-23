# 简介
EzeOrm 是一个使用java 编写的简易ORM（Object Relational Mapping 对象关系映射）框架。 关于ORM框架的简介请参考  [对象关系映射——维基百科](https://zh.wikipedia.org/wiki/%E5%AF%B9%E8%B1%A1%E5%85%B3%E7%B3%BB%E6%98%A0%E5%B0%84) 。帮助程序员更加简单的将自己的java 程序与数据库相连接。

## 数据库支持
 - mysql
 - ~~SQLite~~ 尚未支持

## 项目状态
### 已实现功能
 - 注解
 - 数据表的创建
 - 数据的增加
    - 单行插入
    - 多行插入
 - 数据的查找
    - 全部查找
    - 按条件查找
 - 数据的删除
    - 单行数据删除
    - 按条件删除
    - 整个数据表删除
 
### 未实现
 - 数据的更新
 - 外键
 - 数据表结构的修改
 - 其他特性...
 
# 使用示例
### 创建Entity实体类

    import com.ericwyn.ezeorm.annotation.AutoIncrement;
    import com.ericwyn.ezeorm.annotation.Column;
    import com.ericwyn.ezeorm.annotation.ColumnType;
    import com.ericwyn.ezeorm.annotation.Entity;
    import com.ericwyn.ezeorm.annotation.PrimaryKey;
    
    import java.util.Date;
    
    /**
     *
     * user的实体类
     * Created by Ericwyn on 17-11-20.
     */
    @Entity(table = "user")
    public class User {
    
        @PrimaryKey
        @AutoIncrement
        @Column(type = ColumnType.INT)
        private Long id;
    
        @Column(type = ColumnType.TEXT,notNull = true)
        private String name;
    
        @Column(type = ColumnType.INT,notNull = true)
        private int age;
    
        @Column(type = ColumnType.TEXT,notNull = true)
        private String sex;
    
        @Column(type = ColumnType.DATETIME,notNull = true)
        private Date timeStamp;
    
        @Column(type = ColumnType.INT)
        private boolean good;
    
        public User() {
    
        }
        
        //省略的Getter 和 Setter 方法
    
    }

### 创建EzeDbServer并使用

    package com.ericwyn.ezeorm;
    
    import com.ericwyn.ezeorm.entity.Admin;
    import com.ericwyn.ezeorm.entity.User;
    import java.util.Date;
    import java.util.List;
    
    /**
     * Created by Ericwyn on 17-11-20.
     */
    public class Main {
        public static void main(String[] args) throws Exception{
            //创建EzeDbServer
            EzeDbServer<User> userServer=new EzeDbServer.Builder<User>()
                    .setEntityClass(User.class)
                    .create();
            EzeDbServer<Admin> adminServer=new EzeDbServer.Builder<Admin>()
                    .setEntityClass(Admin.class)
                    .create();
    
            //后续的增删改查操作
            //......
            
        }
    }
    
### 插入数据
    //单条数据插入
    User user=new User();
    user.setName("girlName2");
    user.setAge(15);
    user.setTimeStamp(new Date());
    user.setSex("girl");
    user.setGood(false);

    userServer.insert(user);
    
    //插入多条数据
    ArrayList<User> userList=new ArrayList<>();
    for (int i=0;i<10;i++){
        User listUserTemp=new User();
        listUserTemp.setName("userNameTemp"+i);
        listUserTemp.setAge(i);
        listUserTemp.setTimeStamp(new Date());
        listUserTemp.setSex("girl");
        listUserTemp.setGood(false);
        userList.add(listUserTemp);
    }

    userServer.insertList(userList);
    
### 查询数据

    //查询所有数据
    List<User> allUser = userServer.findAll();
    for (User userTemp:allUser){
        System.out.println(userTemp.getId()+" "+userTemp.getName()+" "+userTemp.getSex()+" "
                +userTemp.getAge()+" "+userTemp.getTimeStamp()+" "+userTemp.isGood());
    }

    //自定义条件查询
    List<User> allGirl = userServer.findByAttributes("sex = 'girl'");
    for (User userTemp:allGirl){
        System.out.println(userTemp.getId()+" "+userTemp.getName()+" "+userTemp.getSex()+" "
                +userTemp.getAge()+" "+userTemp.getTimeStamp());
    }

    //多条件查询
    List<User> allGirl2=userServer.findByAttributes("sex = 'girl'","age > 11");
    for (User userTemp:allGirl2){
        System.out.println(userTemp.getId()+" "+userTemp.getName()+" "+userTemp.getSex()+" "
                +userTemp.getAge()+" "+userTemp.getTimeStamp());
    }
    
 - 注 1：自定义条件查询、多条件查询当中的条件参数，需与MySql查询语句WHERE后条件语句写法一致

### 删除数据

    //删除单条数据
    List<User> allGirl = userServer.findByAttributes("id=1");
    for (User userTemp:allGirl){
        userServer.delete(userTemp);
    }
    
    //自定义条件删除
    userServer.deleteByAttributes("sex ='boy'");
    
    //多条件删除
    userServer.deleteByAttributes("sex ='boy'","age >= 15");
    
    //删除表中所有数据
    userServer.deleteAll();
    
    //删除列表 （不推荐使用该方法，推荐使用多条件删除）
    List<User> allGirl=userServer.findByAttributes("sex = 'girl'");
    userServer.deleteList(allGirl);

    //删除该数据表
    userServer.dropTable();
    
 - 注 1：删除单条数据时候，会根据传入的 Entity 对象所有属性构建删除的SQL语句，所以有可能无法构造出一个与数据库中的某一列完全映射的实体类对象。所以使用这个方法时候，推荐是先通过查询方法获取实体类对象之后在将其作为参数传入
 - 注 2：一般情况下更推荐使用传入条件限定语句进行删除操作
 - 注 3：自定义条件删除、多条件删除当中的条件参数，需与MySql删除语句WHERE后条件语句写法一致

## 对象关系映射表
### **mysql**里的映射
    
|    java 类型    | mysql 类型 |
| :--: | :--: |
| int、Integer、byte、Byte、short、Short、long、Long、Boolean、boolean |  INT |
| Long       |  BIGINT |
| float、Float、double、Double       |  DOUBLE |
| Date       |  DATETIME |
| char Char String       |  TEXT |

## 主要注解说明
### `@Entity`
 - 使用在类当中，标记实体类
 - `table` 属性设定表的名字
### `@Column`
 - 使用在类的变量当中，代表该类是数据表中的一个字段
 - 通过类的属性名转换获取对应数据表中的字段名字。
 - `type` 设定字段类型  （只能从ColumnType 中选择）
 - `notNull` 设定字段是否可为空 （默认为`false`,代表可为空）
### `@AutoIncrement`
 - 使用在类变量当中，代表该字段是自动增加的
### `@PrimaryKey`
 - 使用在类变量当中，代表该字段是主键
 

## 配置文件`ezeorm.cfg` 说明
整合ConfigGet工具进行配置文件的管理和读取, 关于该工具的更多信息可查看 [ConfigGet](https://github.com/Ericwyn/JavaUtil/blob/master/src/ConfigGet/README.md)


    db_connect_url = jdbc:mysql://localhost:3306/db_name?characterEncoding=utf-8&useUnicode=true
    db_account = root 
    db_password = password 


# 主要架构设计备注
 - 通过`EzeDbServer.Builder` 创建EzeDbServer,每一个`Entity`类，绑定一个EzeDbServer ，而后再绑定一张数据表
 - 所有的`EzeDbServer` 共同使用`EzeSql`类，`EzeSql`是`EzeOrm`最底层的jdbc封装，所有的构建好的sql语句，直接在`EzeSql类`中执行。
 - 所以`EzeSql`类中，有所有类共用的`Connection` 和`Statement`，以及sql语句执行方法。
 - 各个实体类对象绑定的`EzeDbServer`，都要通过`MySQLCodeBuilder` 来生成增删改查使用的SQL语句，而后再传输到`EzeSql`的sql执行方法当中

# 其他说明
## 关于表结构更新
所有的表格更新都采用同一种方式，参考`spring.jpa.properties.hibernate.hbm2ddl.auto`属性，与`spring.jpa.properties.hibernate.hbm2ddl.auto`的 `update` 模式相同。第一次加载时根据Entity类会自动建立起表的结构（前提是先建立好数据库），以后加载时根据`Entity`类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行，表结构是不会在部署之初被马上建立起来的，是要等应用第一次运行起来后才会。后期可能会使用一个新的注解或者配置项，来达到像Hibernate 那样的表结构更新模式设置。

## 关于Entity类中属性的命名
 - 非 Boolean 或非 boolean 类型的变量统一使用驼峰发命名，如`registerDate`
 - Boolean 或者 boolean 类型的变量无需在变量名头部加入 `is` ，例如不能是`isGood`，而应该直接是`good`