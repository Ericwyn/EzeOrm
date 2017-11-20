package com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.dbTools.EzeSql;
import com.ericwyn.ezeorm.dbTools.sqlbuilder.MySQLCodeBuilder;
import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * orm类
 *
 * Created by Ericwyn on 17-11-20.
 */
public class EzeDbServer {
    private Class entityClass;
    private EzeSql ezeSql;
    private Connection connection;
    private MySQLCodeBuilder coderBuilder;
    private TableObj table;


    public static class Builder{
        private Class entityClass;

        public Builder(){

        }
        public Builder setEntityClass(Class tableClass){
            this.entityClass =tableClass;
            return this;
        }

        public EzeDbServer create(){
            return new EzeDbServer(this);
        }

    }

    private EzeDbServer(Builder builder){
        this.entityClass=builder.entityClass;
        try {
            table = ParseTools.parseEntity(entityClass);
        } catch (EzeExpection ezeExpection) {
            ezeExpection.printStackTrace();
        }
        //初始化创建sql语句构建器
        coderBuilder=new MySQLCodeBuilder();
        ezeSql=new EzeSql();
        //初始化数据库Connection
        ezeSql.initConnection();
        connection=EzeSql.conn;
        //数据库连接启动的时候对表格进行检查
        //也就是会自动创建表
        initTable(table);
    }

    @Override
    public String toString() {
        return ezeSql.getUrl()+entityClass.getName();
    }


    /**
     * 表格是否已经创建
     * @param tableObj
     * @return
     */
    private boolean isTableCreate(TableObj tableObj){
        try {
            ResultSet rs  = connection.getMetaData().getTables(null, null, table.getTableName() , null );
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建表格
     * @param tableObj
     */
    private void createTable(TableObj tableObj){
        ezeSql.runSQL(coderBuilder.createTable(tableObj));
    }

    /**
     * 判断表格是否发生了变化
     * @param table
     * @return
     */
    private boolean isTableChange(TableObj table){
        System.out.println("表格发生变化");

        return false;
    }

    /**
     *
     * @param table
     */
    private void updateTable(TableObj table){
        System.out.println("表格更新");
        //所有的表格更新都采用同一种方式，参考spring.jpa.properties.hibernate.hbm2ddl.auto 属性
        //与spring.jpa.properties.hibernate.hbm2ddl.auto 的 update模式相同
        //第一次加载时根据model类会自动建立起表的结构（前提是先建立好数据库）
        //以后加载hibernate时根据model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。
        //表结构是不会在部署之初被马上建立起来的，是要等应用第一次运行起来后才会。

    }


    /**
     * 数据库连接启动的时候对表格进行检查
     *
     * 判断表格是否存在
     *      存在：
     *          判断是否变化
     *              变化：
     *                  修改
     *              没变化：
     *                  保持
     *      不存在：
     *           创建表格
     *
     * @param table
     * @return
     */
    public void initTable(TableObj table){
        //判断表是否存在
        if(isTableCreate(table)){
            //已经存在，判断是否改变
            if(isTableChange(table)){

            }
        }else {
            //不存在
            createTable(table);
        }
    }

    public void add(){

    }
    public void delete(){

    }


}
