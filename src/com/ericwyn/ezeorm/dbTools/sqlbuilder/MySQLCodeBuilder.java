package com.ericwyn.ezeorm.dbTools.sqlbuilder;

import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 *
 * Mysql语句构造器
 *
 * 全部使用StringBuilder来构建，
 * StringBuilder 为单线程设计，
 * 多线程下可能会不安全。
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
public class MySQLCodeBuilder {


    /**
     * 生成建表语句
     *
     * @param tableObj 传入一个TableObj 对象，表明需要生成的
     * @return 返回创建数据表的语句
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
        res.append("PRIMARY KEY ( ").append(tableObj.getPrimaryKeyName()).append(" )\n");
        res.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        return res.toString();
    }

    /**
     * 生成数据库插入语句
     *
     * <code>
     * INSERT INTO user (`name`, `age`, `sex`, `time_stamp`)
     *      VALUES
     *      ('testName', 11, 'girl', "2017-11-22 19:09:01");
     * </code>
     *
     * @param tableObj 传入Tableobj对象，然后
     * @param object 传入 Entity 实体类对象，需要与TableObj对应。
     * @return 返回插入语句，类似于
     * <code>
     * INSERT INTO user (`name`, `age`, `sex`, `time_stamp`)
     *      VALUES
     *      ('testName', 11, 'girl', "2017-11-22 19:09:01");
     * </code>
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
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            stringBuilder.append(filedBuilder.toString().substring(0,filedBuilder.length()-2)).append(")\n")
                    .append("VALUES\n")
                    .append(valueBuilder.toString().substring(0,valueBuilder.length()-2)).append(");");
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回查询所有数据的sql语句
     *
     * @param tableObj 传入TableObj对象代表需要查询的数据表
     * @return 返回sql语句，类似于
     * <code>select * from `user` ;</code>
     */
    public String findAll(TableObj tableObj){
        return "select * from `"+tableObj.getTableName()+"`;";
    }

    /**
     * 返回条件查询 sql 语句
     *
     * @param tableObj 传入TableObj 代表需要查询的数据表
     * @param attributes 传入sql 语句中的WHERE 后的条件限定语句
     * @return 返回sql语句 ，类似于
     * <code>select * from `user` WHERE age=19;</code>
     */
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

    /**
     * 返回删除数据库的 sql 语句<br>
     *     该方法构建的语句会包含对数据表中所有字段的限制语句，
     *     也就是object必须和数据表中的某一行完全对应，
     *     所有的属性都必须与哪一行的字段相符合。
     *     所以该方法的传入对象一般都应该是通过查询方法寻找到的对象。
     *
     * @param tableObj 传入TableObj 代表需要查询的数据表
     * @param object    传入需要删除的对象，通过TableObj对象来构建反射来获取对象属性并且放入到sql语句的构造之中
     *                  该对象最好是通过find方法查找的到的对象。
     * @return 返回sql语句，类似于
     *      <code>
     *          DELETE FROM `user`
     *              WHERE `name`='girlName2' AND `age`=15 AND `sex`='boy'
     *              AND `time_stamp`='2017-12-17 12:02:00' AND `good`=0 ;
     *      </code>
     */
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

    /**
     * 构建 sql 语句，删除表中所有数据
     *
     * @param tableObj  传入TableObj对象，
     * @return 返回 sql 语句，类似于
     *  <code>DELETE FROM `user`</code>
     */
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
    /**
     * 构建 sql 语句，删除数据表
     *
     * @param tableObj  传入TableObj对象，
     * @return 返回 sql 语句，类似于
     *  <code>DROP TABLE `user`</code>
     */
    public String dropTable(TableObj tableObj){
        return "DROP TABLE `"+tableObj.getTableName()+"`;";
    }

    /**
     * 构建 sql 语句，更新数据表中的一行数据
     *
     * 该方法先通过tableObj对象，寻找到表的主键（在存在多个主键的情况下只会获取第一个主键）。
     * 而后通过该主键来将数据表某一列的数据全部更新成object当中的数据。
     *
     * @param tableObj  传入TableObj对象
     * @param object 需要更新的具体object对象，理论上来讲属性只需要包含一个主键就可以了。
     * @return 返回 sql 语句，类似于
     *  <code>
     *      UPDATE `user`
     *      SET `name`='girlName2' , `age`=15 , `sex`='男' ,
     *          `time_stamp`='2017-12-17 13:20:06' , `good`=false
     *      WHERE `id`=2;
     *  </code>
     */
    public String update(TableObj tableObj, Object object){
        try {
            //通过反射获取主键的值
            String primaryKeyAndValue="";
            String primaryKeyName = tableObj.getPrimaryKey().get(0);
            Method[] methods=object.getClass().getDeclaredMethods();
            if (primaryKeyName == null || primaryKeyName.equals("")){
                throw new EzeExpection("该表不存在主键字段，无法使用该方法更新");
            }else {
                for (Method method:methods){
                    String methodNameTemp = method.getName().toLowerCase();
                    if (method.getParameterCount() != 0) {
                        continue;
                    }
                    String fieldNameTemp=ParseTools.getFieldNameFormColumnName(primaryKeyName);
                    if(methodNameTemp.equals("get"+fieldNameTemp)
                            || methodNameTemp.equals("is"+fieldNameTemp)){
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
                    if (columnObj.getName().equals(tableObj.getPrimaryKey().get(0))){
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

    /**
     * 生成更新表名称的方法
     * @param obj 需要改名的数据表的映射对象
     * @param newTableName  新的数据表名称
     * @return  返回构建的sql 语句,类似于
     *      <code>RENAME TABLE `user` TO `new_user`;</code>
     */
    public String renameTable(TableObj obj,String newTableName){
        return "RENAME TABLE `"+obj.getTableName()+"` TO `"+newTableName+"`;";
    }

}
