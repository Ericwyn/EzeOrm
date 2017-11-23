package com.ericwyn.ezeorm.dbTools.sqlbuilder;

import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.EzeConfig;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
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

    /**
     * 生成语句类似
     * INSERT INTO user (name, age, sex, time_stamp)
     *      VALUES
     *      ("testName", 11, "girl", "2017-11-22 19:09:01");
     * @param tableObj
     * @param object
     * @return
     */
    public String insert(TableObj tableObj,Object object){
        try {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("INSERT INTO ").append(tableObj.getTableName()).append(" ");
            StringBuilder filedBuilder=new StringBuilder("(");
            StringBuilder valueBuilder=new StringBuilder("(");
            //获取器方法
            Method[] declaredMethods = object.getClass().getMethods();
            //便利表中的没一个字段
            for (ColumnObj columnObj:tableObj.getColumns()){
                //遍历所有的方法，找到获取字段对应的属性的值的方法
                for (Method method:declaredMethods){
                    try {
                        String methodNameTemp=method.getName().toLowerCase();
                        //找到了对应的属性获取方法
                        if(method.getParameterCount()!=0){
                            continue;
                        }
                        if(methodNameTemp.equals("get"+columnObj.getName().replaceAll("_",""))
                                || methodNameTemp.equals("is"+columnObj.getName().replaceAll("_",""))
                                ){
                            Object invoke = method.invoke(object);
                            if(invoke!=null){
                                filedBuilder.append(columnObj.getName()+", ");

                                switch (columnObj.getType()) {
                                    case "INT":
                                    case "DOUBLE":
                                    case "BIGINT":
                                        valueBuilder.append(invoke.toString() + ", ");
                                        break;
                                    case "TEXT":
                                        valueBuilder.append("\"" + invoke.toString() + "\", ");
                                        break;
                                    case "DATETIME":
                                        if (invoke instanceof Date) {
                                            valueBuilder.append("\"" + ParseTools.sdfForDATATIME.format((Date) invoke) + "\", ");
                                        } else {
                                            throw new EzeExpection(columnObj.getName() + "字段 java 时间格式错误，请使用 java.util.Date()");
                                        }
                                        break;
                                    case "BOOLEAN":
                                        if((Boolean)invoke){
                                            valueBuilder.append("1, ");
                                            break;
                                        }else {
                                            valueBuilder.append("0, ");
                                            break;
                                        }
                                }
                            }

                        }
//                        if((methodNameTemp.contains("get") && methodNameTemp.contains(columnObj.getName().replaceAll("_","")))
//                                || (methodNameTemp.contains("is") && methodNameTemp.contains(columnObj.getName().replaceAll("_","")))){
//                            Object invoke = method.invoke(object);
////                            if(invoke == null){
////                                //如果是自增的话，那么值可以为空，不用管
////                                if(!columnObj.isAutoIncrement()){
////                                    //如果
////                                    if(columnObj.isNotNull()){
////                                        throw new EzeExpection(columnObj.getName()+"字段不允许为空值");
////                                    }else {
////                                        countTemp++;
////                                    }
////                                }else {
////                                    countTemp++;
////                                }
////                            }else {
////                                if(countTemp==tableObj.getColumns().size()-1){
////                                    filedBuilder.append(columnObj.getName()+" ");
////                                    valueBuilder.append(invoke.toString()+" ");
////                                }else {
////                                    filedBuilder.append(columnObj.getName()+", ");
////                                    valueBuilder.append(invoke.toString()+", ");
////                                }
////                                countTemp++;
////                            }
//                            if(invoke!=null){
//                                filedBuilder.append(columnObj.getName()+", ");
//                                valueBuilder.append(invoke.toString()+", ");
//                            }
//                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            stringBuilder.append(filedBuilder.toString().substring(0,filedBuilder.length()-2)).append(")\n")
                    .append("VALUES\n")
                    .append(valueBuilder.toString().substring(0,valueBuilder.length()-2)).append(");");
//            System.out.println(stringBuilder.toString());
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public String findAll(TableObj tableObj){
        return "select * from "+tableObj.getTableName()+";";
    }

    //按参数进行查询
    public String findByAttributes(TableObj tableObj,String... attributes){
        String temp="";
        if(attributes.length==1){
            temp+=attributes[0];
        }else {
            for(int i=0;i<attributes.length;i++){
                if(i!=attributes.length-1){
                    temp+=attributes[i]+" AND ";
                }else {
                    temp+=attributes[i];
                }
            }
        }
        String res="select * from "+tableObj.getTableName()+" WHERE "+temp+" ;";
        return res;
    }
}
