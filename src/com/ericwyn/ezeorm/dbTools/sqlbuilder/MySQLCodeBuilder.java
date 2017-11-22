package com.ericwyn.ezeorm.dbTools.sqlbuilder;

import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * sql语句构造器
 *
 * q:全部使用StringBuilder 来构建的话，StringBuilder 为单线程设计，是否会存在多线程下的问题？
 *
 *
 * Created by Ericwyn on 17-11-20.
 */
public class MySQLCodeBuilder {


    /**
     * 建表
     * @param tableObj
     * @return
     */
    public String createTable(TableObj tableObj){
        StringBuilder res=new StringBuilder();

        res.append("CREATE TABLE IF NOT EXISTS ").append(tableObj.getTableName()).append(" (\n");

        List<ColumnObj> list=tableObj.getColumns();

        for (ColumnObj columnObj:list){
            res.append(columnObj.getName()).append(" ");
            res.append(columnObj.getType()).append(" ");
            if(columnObj.isNotNull()){
                res.append("NOT NULL ");
            }
            if(columnObj.isAutoIncrement()){
                res.append("AUTO_INCREMENT ");
            }

            res.append(",\n");
        }
        res.append("PRIMARY KEY ( ").append(tableObj.getPrimaryKey()).append(" )\n");
        res.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        return res.toString();
    }


    public String insert(TableObj tableObj,Object object){
        String a="INSERT INTO table_name ( field1, field2,...fieldN )\n" +
                "                       VALUES\n" +
                "                       ( value1, value2,...valueN );";

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("INSERT INTO table_name (");
        StringBuilder builderField=new StringBuilder("(");
        StringBuilder valueField=new StringBuilder("(");
        Method[] declaredMethods = object.getClass().getMethods();
        Field[] fields = object.getClass().getFields();
        for (ColumnObj columnObj:tableObj.getColumns()){
            builderField.append(columnObj.getName());
            for (Method method:declaredMethods){
//                try {
////                    if(method.getName().contains("get") || method.getName().contains(""));
//
////                    Object invoke = method.invoke(object);
//
//
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
            }


        }


        return null;
    }

}
