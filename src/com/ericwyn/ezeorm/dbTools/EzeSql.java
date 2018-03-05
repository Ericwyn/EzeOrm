package com.ericwyn.ezeorm.dbTools;

import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.EzeConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * sql 连接管理事宜
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
public class EzeSql {
    //共用的conn，静态
    public static Connection conn;
    private static String url= EzeConfig.db_connect_url;
    private static SimpleDateFormat sdf = new SimpleDateFormat("MMdd_hhmmss");

    public EzeSql(){

    }

    /**
     * 初始化连接的方法，只能由EzeDbServer初始化时候，新建了EzeSql对象后调用，单整个程序运行过程中，该初始化只会被运行一次。<br>
     *  1.加载JDBC Mysql 驱动<br>
     *  2.初始化 公用 Connection<br>
     *  3.通过Connection 获取 statement<br>
     */
    public void initConnection() {
        if(conn==null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("成功加载jdbc Mysql 驱动");
                //初始化conniction
                conn = DriverManager.getConnection(url,EzeConfig.db_account,EzeConfig.db_password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e2){
                e2.printStackTrace();
            }
        }
    }

    /**
     * 得到数据库连接的url
     * @return 返回数据库连接url
     */
    public String getUrl() {
        return url;
    }

    public static Connection getConn() {
        return conn;
    }

    /**
     * EzeOrm 最底层的 sql 运行方法，这个方法无返回值，用以执行无需数据返回的sql语句，例如数据的删除，数据的更新等
     * @param sql 传入需要执行的sql语句
     */
    public void runSQL(String sql) {
        if(EzeConfig.db_show_sql){
            System.out.println("[DEBUG_EzeOrm]"+sdf.format(new Date())+" : "+sql);
        }
        try {
            Statement statement=conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * EzeOrm 最底层的 sql 运行方法，该方法返回一个ResultSet，用以执行查询一类的需要返回值的sql语句
     * @param sql 传入需要执行的sql语句
     * @return  返回处理结果
     */
    public ResultSet runSQLForRes(String sql){
        if(EzeConfig.db_show_sql){
            System.out.println("[DEBUG_EzeOrm] "+sdf.format(new Date())+" : "+sql);
        }
        try {
            Statement statement=conn.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
