package com.ericwyn.ezeorm.tool;

/**
 * EzeOrm 的配置文件读取及存储类
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
public class EzeConfig {
    private static final ConfigGet conf = new ConfigGet("ezeorm.cfg",true);
    public static final String db_connect_url=conf.getValue("null","db_connect_url");
    public static final String db_account=conf.getValue("root","db_account");
    public static final String db_password=conf.getValue("password","db_password");
    public static final String db_update_model=conf.getValue("never_set","db_update_model");

}
