package com.ericwyn.ezeorm.tool;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class EzeConfig {
    private static final ConfigGet conf = new ConfigGet("ezeorm.cfg",true);
    public static final String db_connect_url=conf.getValue("null","db_connect_url");
    public static final String db_account=conf.getValue("root","db_account");
    public static final String db_password=conf.getValue("password","db_password");

}
