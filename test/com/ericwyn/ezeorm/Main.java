package com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.entity.Admin;
import com.ericwyn.ezeorm.entity.User;

import java.sql.ResultSet;
import java.util.ArrayList;
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
//        EzeDbServer<Admin> adminServer=new EzeDbServer.Builder<Admin>()
//                .setEntityClass(Admin.class)
//                .create();

//        //插入单条数据
//        User user=new User();
//        user.setName("girlName2");
//        user.setAge(15);
//        user.setTimeStamp(new Date());
//        user.setSex("boy");
//        user.setGood(false);
//
//        userServer.insert(user);
//
//        插入多条数据
//        ArrayList<User> userList=new ArrayList<>();
//        for (int i=0;i<10;i++){
//            User listUserTemp=new User();
//            listUserTemp.setName("userNameTemp"+i);
//            listUserTemp.setAge(i);
//            listUserTemp.setTimeStamp(new Date());
//            listUserTemp.setSex("girl");
//            listUserTemp.setGood(false);
//            userList.add(listUserTemp);
//        }
//
//        userServer.insertList(userList);
//
//        Admin admin=new Admin();
//        admin.setAccount("test");
//        admin.setPw("testPw");
//
//        adminServer.insert(admin);
//
//        //查询所有数据
//        List<User> allUser = userServer.findAll();
//        for (User userTemp:allUser){
//            System.out.println(userTemp.getId()+" "+userTemp.getName()+" "+userTemp.getSex()+" "
//                    +userTemp.getAge()+" "+userTemp.getTimeStamp()+" "+userTemp.isGood());
//        }
//
//        List<Admin> allAdmin = adminServer.findAll();
//        for (Admin adminTemp:allAdmin){
//            System.out.println(adminTemp.getId()+" "+adminTemp.getAccount()+" "+adminTemp.getPw());
//        }

//        //自定义条件查询
//        List<User> allGirl = userServer.findByAttributes("id = 17");
//        for (User userTemp:allGirl){
//            System.out.println(userTemp.getId()+" "+userTemp.getName()+" "+userTemp.getSex()+" "
//                    +userTemp.getAge()+" "+userTemp.getTimeStamp());
//        }

//        //多条件查询
//        List<User> allGirl2=userServer.findByAttributes("sex = \"girl\"","age > 11");
//        for (User userTemp:allGirl2){
//            System.out.println(userTemp.getId()+" "+userTemp.getName()+" "+userTemp.getSex()+" "
//                    +userTemp.getAge()+" "+userTemp.getTimeStamp());
//        }

//        //更新数据
//        List<User> allGirlTemp = userServer.findByAttributes(" sex ='girl' ");
//        for (User userTemp:allUser){
//            userTemp.setSex("boy");
//            userServer.updata(userTemp);
//
//        }

//        userServer.runQuery("DROP TABLE user");
//        ResultSet resultSet = userServer.runQueryForRes("SELECT * FROM user ");
//        List<User> users = userServer.parseResultSet(resultSet);
//        for (User user:users){
//            System.out.println(user.getName());
//        }

    }
}
