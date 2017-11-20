package test.com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.EzeDBBuilder;

import test.com.ericwyn.ezeorm.entity.User;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        EzeDBBuilder.EzeDB userServer=new EzeDBBuilder()
                .url("aaa")
                .setClass(User.class)
                .build();
        System.out.println(userServer);
    }
}
