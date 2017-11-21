package com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.dbTools.EzeSql;
import com.ericwyn.ezeorm.dbTools.sqlbuilder.MySQLCodeBuilder;
import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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
     * 通过数据表的名字来获取表对象，从而来判断表结构是否有更新
     *
     * @param table_name
     * @return
     * @throws SQLException
     */
    private TableObj getTableObj(String table_name) {
        try {
            DatabaseMetaData  m_DBMetaData= connection.getMetaData();
            ResultSet tableRet = m_DBMetaData.getTables(null, "%",table_name,new String[]{"TABLE"});
        /*其中"%"就是表示*的意思，也就是任意所有的意思。其中m_TableName就是要获取的数据表的名字，如果想获取所有的表的名字，就可以使用"%"来作为参数了。*/
            TableObj tableObj=new TableObj();
            //3. 提取表的名字。
            while(tableRet.next()){
                if(tableRet.getString("TABLE_NAME").equals(table_name)){
                    tableObj.setTableName(table_name);
                    break;
                }
            }
            ResultSet primaryKeys = m_DBMetaData.getPrimaryKeys("test", null, table_name);
            while (primaryKeys.next()){
                tableObj.setPrimaryKey(primaryKeys.getString("COLUMN_NAME"));
            }
            ResultSet colRet = m_DBMetaData.getColumns(null,"%", table_name,"%");
            while(colRet.next()) {
                ColumnObj columnObj=new ColumnObj();
                columnObj.setName(colRet.getString("COLUMN_NAME"));
                columnObj.setType(colRet.getString("TYPE_NAME"));
                columnObj.setAutoIncrement(colRet.getBoolean("IS_AUTOINCREMENT"));
                columnObj.setNotNull(!colRet.getBoolean("NULLABLE"));
                tableObj.addColum(columnObj);
            }
            return tableObj;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

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
        TableObj oldTableObj=getTableObj(table.getTableName());
        if(table.equals(oldTableObj)){
            System.out.println("表格没有变化");
        }else {
            System.out.println("表格与数据库表对比发生了变化");
        }
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
