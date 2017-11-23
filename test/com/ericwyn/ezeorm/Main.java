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
        EzeDbServer<User> userServer=new EzeDbServer.Builder<User>().setEntityClass(User.class).create();
        EzeDbServer<Admin> adminServer=new EzeDbServer.Builder<Admin>().setEntityClass(Admin.class).create();

        //插入数据
        User user=new User();
        user.setName("girlName2");
        user.setAge(11);
        user.setTimeStamp(new Date());
        user.setSex("girl");
        user.setGood(false);

        userServer.insert(user);

        Admin admin=new Admin();
        admin.setAccount("test");
        admin.setPw("testPw");

        adminServer.insert(admin);


        //查询数据
        List<User> allUser = userServer.findAll();
        for (User userTemp:allUser){
            System.out.println(userTemp.getId()+" "+user.getName()+" "+userTemp.getSex()+" "+userTemp.getAge()+" "+userTemp.getTimeStamp());
        }

        List<Admin> allAdmin = adminServer.findAll();
        for (Admin adminTemp:allAdmin){
            System.out.println(adminTemp.getId()+" "+adminTemp.getAccount()+" "+adminTemp.getPw());
        }

    }
}
