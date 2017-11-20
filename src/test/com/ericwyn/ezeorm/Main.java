package test.com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.EzeDdServerBuilder;

import test.com.ericwyn.ezeorm.entity.User;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        EzeDdServerBuilder.EzeDdServer userServer=new EzeDdServerBuilder()
                .setClass(User.class)
                .build();
        System.out.println(userServer);
    }
}
