package com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.dbTools.EzeSql;
import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.sql.Connection;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class EzeDdServerBuilder {

    private Class tableClass =null;



    public EzeDdServerBuilder(){

    }

    public EzeDdServerBuilder setClass(Class tableClass){
        this.tableClass=tableClass;
        return this;
    }

    public EzeDdServer build() throws EzeExpection{

        if(tableClass ==null){
            throw new EzeExpection("builder 的对象映射类为空，请使用 setClass() 方法设置对象映射类");
        }else{
            return new EzeDdServer(tableClass);
        }
    }


    /**
     * orm 类
     *
     * 初始化的时候，自动查找是否存在一个这样的表格，以及表格是否有修改。
     *
     */
    public class EzeDdServer {
        private Class tableClass;
        private EzeSql ezeSql;
        private Connection connection;

        private TableObj table;
//        public static
        private EzeDdServer(Class tableClass){
            this.tableClass=tableClass;
            try {
                table = ParseTools.parseEntity(tableClass);
            } catch (EzeExpection ezeExpection) {
                ezeExpection.printStackTrace();
            }
            ezeSql=new EzeSql();
            //初始化数据库Connection
            ezeSql.initConnection();
            connection=EzeSql.conn;
            initTable(table);
        }

        @Override
        public String toString() {
            return ezeSql.getUrl()+tableClass.getName();
        }


        /**
         * 表格是否已经创建
         * @param tableObj
         * @return
         */
        private boolean isTableCreate(TableObj tableObj){



            return false;
        }


        /**
         * 创建表格
         * @param tableObj
         */
        private void createTable(TableObj tableObj){

        }

        /**
         * 判断表格是否发生了变化
         * @param obj
         * @return
         */
        private boolean isTableChange(TableObj obj){
            return false;
        }

        /**
         *
         * @param table
         */
        private void updateTable(TableObj table){

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
                //已经存在
                if(isTableChange(table)){

                }
            }else {
                //不存在
                createTable(table);
            }
        }

    }
}
