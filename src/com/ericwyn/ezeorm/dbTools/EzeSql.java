package com.ericwyn.ezeorm.dbTools;

import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.EzeConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * sql 连接管理事宜
 *
 * Created by Ericwyn on 17-11-20.
 */
public class EzeSql {
    //共用的conn
    public static Connection conn;
    private String url= EzeConfig.db_connect_url;
    //共用的statement
    private Statement statement=null;


    public EzeSql(){

    }

    public void initConnection() {
        if(conn==null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("成功加载jdbc Mysql 驱动");
                //初始化conniction
                conn = DriverManager.getConnection(url,EzeConfig.db_account,EzeConfig.db_password);
                this.statement=conn.createStatement();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e2){
                e2.printStackTrace();
            }
        }
    }
    public String getUrl() {
        return url;
    }

    public static Connection getConn() {
        if(conn==null){
            new EzeSql().initConnection();
        }
        return conn;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public void runSQL(String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runSQLForRes(String sql){
        try {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
