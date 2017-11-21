package com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.entity.User;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        EzeDbServer.Builder builder=new EzeDbServer.Builder();
        builder.setEntityClass(User.class);
        EzeDbServer userServer = builder.create();
    }
}
