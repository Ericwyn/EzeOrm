package com.ericwyn.ezeorm.dbTools.sqlbuilder;

import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.EzeConfig;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.io.File;
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

        res.append("CREATE TABLE IF NOT EXISTS `").append(tableObj.getTableName()).append("` (\n");

        List<ColumnObj> list=tableObj.getColumns();

        for (ColumnObj columnObj:list){
            res.append("`").append(columnObj.getName()).append("` ");
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
     * INSERT INTO user (`name`, `age`, `sex`, `time_stamp`)
     *      VALUES
     *      ('testName', 11, 'girl', "2017-11-22 19:09:01");
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
                                filedBuilder.append("`").append(columnObj.getName()+"`, ");

                                switch (columnObj.getType()) {
                                    case "INT":
                                    case "DOUBLE":
                                    case "BIGINT":
                                        //这里略过了对INT 的boolean的判断，因为直接插入后在mysql 里面就会变成0、1了
                                        valueBuilder.append(invoke.toString() + ", ");
                                        break;
                                    case "TEXT":
                                        valueBuilder.append("'" + invoke.toString() + "', ");
                                        break;
                                    case "DATETIME":
                                        if (invoke instanceof Date) {
                                            valueBuilder.append("'" + ParseTools.sdfForDATATIME.format((Date) invoke) + "', ");
                                        } else {
                                            throw new EzeExpection(columnObj.getName() + "字段 java 时间格式错误，请使用 java.util.Date()");
                                        }
                                        break;
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
        return "select * from `"+tableObj.getTableName()+"`;";
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
        String res="select * from `"+tableObj.getTableName()+"` WHERE "+temp+" ;";
        return res;
    }

    //删除一行数据
    public String delete(TableObj tableObj,Object object){
        try {
            StringBuilder stringBuilder=new StringBuilder();
            StringBuilder valueBuilder=new StringBuilder();
            //获取器方法
            Method[] declaredMethods = object.getClass().getMethods();
            //遍历表中所有字段
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
                                switch (columnObj.getType()) {
                                    case "INT":
                                    case "DOUBLE":
                                    case "BIGINT":
                                        if(invoke.toString().equals("true")){
                                            valueBuilder.append("`").append(columnObj.getName()+"`="+1 + " AND ");
                                        }else if(invoke.toString().equals("false")){
                                            valueBuilder.append("`").append(columnObj.getName()+"`="+0 + " AND ");
                                        }else {
                                            valueBuilder.append("`").append(columnObj.getName()+"`="+invoke.toString() + " AND ");
                                        }
                                        break;
                                    case "TEXT":
                                        valueBuilder.append("`").append(columnObj.getName()+"`="+ "'" + invoke.toString() + "' AND ");
                                        break;
                                    case "DATETIME":
                                        if (invoke instanceof Date) {
                                            valueBuilder.append("`").append(columnObj.getName()+"`="+"'" + ParseTools.sdfForDATATIME.format((Date) invoke) + "' AND ");
                                        } else {
                                            throw new EzeExpection(columnObj.getName() + "字段 java 时间格式错误，请使用 java.util.Date()");
                                        }
                                        break;
                                }
                            }

                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            stringBuilder.append("DELETE FROM `").append(tableObj.getTableName()).append("` WHERE ")
                    .append(valueBuilder.toString().substring(0,valueBuilder.length()-4)).append(";");

            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //删除表中所有数据
    public String deleteAll(TableObj tableObj){
        return "DELETE FROM `"+tableObj.getTableName()+"`";
    }
    //通过参数删除
    public String deleteByAttributes(TableObj tableObj,String... attributes){
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
        String res="DELETE from `"+tableObj.getTableName()+"` WHERE "+temp+" ;";
        return res;
    }
    //删表（下一步怕就是跑路了吧（雾...）
    public String dropTable(TableObj tableObj){
        return "DROP TABLE `"+tableObj.getTableName()+"`;";
    }

    public String update(TableObj tableObj, Object object){
        try {
            //通过反射获取主键的值
            String primaryKeyAndValue="";
            String primaryKeyName = tableObj.getPrimaryKey();
            Method[] methods=object.getClass().getDeclaredMethods();
            if (primaryKeyName == null || primaryKeyName.equals("")){
                throw new EzeExpection("该表不存在主键字段，无法使用该方法更新");
            }else {
                for (Method method:methods){
                    String methodNameTemp = method.getName().toLowerCase();
                    if (method.getParameterCount() != 0) {
                        continue;
                    }
                    if((methodNameTemp.contains("get") && methodNameTemp.contains(ParseTools.getFieldNameFormColumnName(primaryKeyName)))
                            || (methodNameTemp.contains("is") && methodNameTemp.contains(ParseTools.getFieldNameFormColumnName(primaryKeyName)))){
                        Object invoke = method.invoke(object);
                        if(invoke!=null){
                            List<ColumnObj> columnObjs=tableObj.getColumns();
                            String keyPrimaryKeyType="";
                            for (ColumnObj columnObj:columnObjs){
                                if(columnObj.getName().equals(primaryKeyName)){
                                    keyPrimaryKeyType=columnObj.getType();
                                    break;
                                }
                            }
                            switch (keyPrimaryKeyType) {
                                case "INT":
                                case "DOUBLE":
                                case "BIGINT":
                                    if(invoke.toString().equals("true")){
                                        primaryKeyAndValue = "`"+primaryKeyName+"`="+1;
                                    }else if(invoke.toString().equals("false")){
                                        primaryKeyAndValue ="`"+primaryKeyName+"`="+0;
                                    }else {
                                        primaryKeyAndValue = "`"+primaryKeyName+"`="+invoke.toString();
                                    }
                                    break;
                                case "TEXT":
                                    primaryKeyAndValue = "`"+primaryKeyName +"`="+"'"+invoke.toString()+"'";
                                    break;
                                case "DATETIME":
                                    if (invoke instanceof Date) {
                                        primaryKeyAndValue = "`"+primaryKeyName +"`="+"'"+ ParseTools.sdfForDATATIME.format((Date) invoke)+"'";
                                    } else {
                                        throw new EzeExpection(primaryKeyName + "字段 java 时间格式错误，请使用 java.util.Date()");
                                    }
                                    break;
                            }
                            break;
                        }else {
                            throw new EzeExpection("无法通过get、is方法获取该映射对象内主键的值，无法更新表");
                        }
                    }
                }
                if(primaryKeyAndValue.equals("")){
                    throw new EzeExpection("无法获取该映射对象内主键的值，有可能是因为没有主键值的get 或者 is 方法，无法更新表");
                }

                //构造更新的语句，代码和delete语句构造方法的一样
                StringBuilder stringBuilder=new StringBuilder();
                StringBuilder valueBuilder2=new StringBuilder();

                //遍历表中所有字段
                for (ColumnObj columnObj:tableObj.getColumns()){
                    if (columnObj.getName().equals(tableObj.getPrimaryKey())){
                        continue;
                    }
                    //遍历所有的方法，找到获取字段对应的属性的值的方法
                    for (Method method:methods){
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
                                    switch (columnObj.getType()) {
                                        case "INT":
                                        case "DOUBLE":
                                        case "BIGINT":
                                            if(invoke.toString().equals("true")){
                                                valueBuilder2.append("`").append(columnObj.getName()+"`=true" + " , ");
                                            }else if(invoke.toString().equals("false")){
                                                valueBuilder2.append("`").append(columnObj.getName()+"`=false" + " , ");
                                            }else {
                                                valueBuilder2.append("`").append(columnObj.getName()+"`="+invoke.toString() + " , ");
                                            }
                                            break;
                                        case "TEXT":
                                            valueBuilder2.append("`").append(columnObj.getName()+"`="+ "'" + invoke.toString() + "' , ");
                                            break;
                                        case "DATETIME":
                                            if (invoke instanceof Date) {
                                                valueBuilder2.append("`").append(columnObj.getName()+"`="+"'" + ParseTools.sdfForDATATIME.format((Date) invoke) + "' , ");
                                            } else {
                                                throw new EzeExpection(columnObj.getName() + "字段 java 时间格式错误，请使用 java.util.Date()");
                                            }
                                            break;
                                    }
                                }

                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                stringBuilder.append("UPDATE `").append(tableObj.getTableName())
                        .append("` SET ")
                        .append(valueBuilder2.toString().substring(0,valueBuilder2.length()-3))
                        .append(" WHERE ")
                        .append(primaryKeyAndValue)
                        .append(";");

                return stringBuilder.toString();
            }
        }catch (IllegalAccessException
                | InvocationTargetException
                | EzeExpection e){
            e.printStackTrace();
            return null;
        }
    }


}
