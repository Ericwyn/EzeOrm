package com.ericwyn.ezeorm.tool;

import com.ericwyn.ezeorm.annotation.AutoIncrement;
import com.ericwyn.ezeorm.annotation.ColumnType;
import com.ericwyn.ezeorm.annotation.PrimaryKey;
import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.annotation.Column;
import com.ericwyn.ezeorm.annotation.Entity;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * EzeOrm 的解析工具。拥有以下解析<br>
 *     1.将一个包含注解的 Class 解析成一个TableObj<br>
 *     2.将字段名解析成java的变量名（要求变量使用驼峰命名法）<br>
 *     3.将java的变量名解析成数据库字段名（要求变量使用驼峰命名法）<br>
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
public class ParseTools {

    public static final SimpleDateFormat sdfForDATATIME=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 通过反射解析包含注解的类成为一个TableObj对象
     *
     * @param cla   映射对象
     * @return  返回一个 Table 对象
     * @throws EzeExpection 返回各种异常，如下
     *      1.类并非使用Entity注解<br>
     *      2.类没有公共的无参构造方法<br>
     */
    public static TableObj parseEntity(Class cla) throws EzeExpection{
        Class classA=cla;
        boolean isEntity=false;
        TableObj tableObj =new TableObj();
        ArrayList<ColumnObj> columns=new ArrayList<>();
        Annotation[] annotations = classA.getAnnotations();
        for (Annotation annotation:annotations){
            if (annotation instanceof Entity){
                isEntity=true;
                Entity entity=(Entity)annotation;
                tableObj.setTableName(entity.table());
            }
        }
        if(!isEntity){
            //如果发现这个类不是实体化对象
            throw new EzeExpection("该类没有Entity 注解，不是实体化对象");
        }else {
            //判断是否存在无参构造方法
            classA.getConstructors();
            boolean haveConstructorMethod=false;
            for (Constructor constructor:classA.getConstructors()){
                if(constructor.getParameterCount()==0){
                    haveConstructorMethod=true;
                }
            }

            if(haveConstructorMethod){
                Field[] fields = classA.getDeclaredFields();
                for (Field field:fields){
                    Annotation[] annotationsOfField=field.getAnnotations();
                    ColumnObj columnObj=new ColumnObj();
                    boolean isColumn=false;
                    boolean primaryFlag=false;
                    boolean autoIncrementFlag=false;
                    for (Annotation annotation:annotationsOfField){
                        if (annotation instanceof Column){
                            Column column=(Column) annotation;
                            isColumn=true;
                            String fieldNameTemp=field.getName();
                            columnObj.setName(getColumnNameFormFieldName(fieldNameTemp));

                            //分割驼峰命名法
                            //通过统一的逻辑由类属性名称获取字段的命名。
//                            columnObj.setName(column.name());
                            String[] r = fieldNameTemp.split("(?=[A-Z])");
                            columnObj.setNotNull(column.notNull());
                            columnObj.setType(column.type().toString());
                        }
                        if(annotation instanceof PrimaryKey){
                            PrimaryKey primaryKey=(PrimaryKey)annotation;
                            tableObj.setPrimaryKey(getColumnNameFormFieldName(field.getName()));
                            primaryFlag=true;
                        }
                        if(annotation instanceof AutoIncrement){
                            AutoIncrement autoIncrement=(AutoIncrement)annotation;
                            columnObj.setAutoIncrement(true);
                            autoIncrementFlag=true;
                        }
                    }
                    if(primaryFlag || autoIncrementFlag){
                        columnObj.setNotNull(true);
                    }
                    if(isColumn){
                        //只有确认了这个是参数才会添加
                        columns.add(columnObj);
                    }

                }
            }else {
                throw new EzeExpection("该Entity 类没有公共的无参构造方法，请添加无参构造方法");
            }
            tableObj.setColumns(columns);
            return tableObj;
        }
    }


    /**
     * 通过类属性名称获取其字段名称，要求使用驼峰命名法
     *
     * @param fieldName 属性名称，例如<code>userName</code>
     * @return 返回的字段名称，如<code>user_name</code>
     */
    public static String getColumnNameFormFieldName(String fieldName){
        String[] r = fieldName.split("(?=[A-Z])");

        if(r.length==0){
            return r[0].toLowerCase();
        }else {
            String temp="";
            for (int i=0;i<r.length;i++){
                if (i!=r.length-1){
                    temp+=r[i].toLowerCase()+"_";
                }else {
                    temp+=r[i].toLowerCase();
                }
            }
            return temp;
        }
    }

    //通过数据库字段名获取其在类中对应的属性名的方法
    /**
     * 通过其字段名称获取类属性名称，要求使用驼峰命名法
     *
     * @param columnName 数据库字段名称，例如<code>user_name</code>
     * @return 返回的属性名称，如<code>userName</code>
     */
    public static String getFieldNameFormColumnName(String columnName){
        String[] r2=columnName.split("_");
        if(r2.length==0){
            return r2[0];
        }else {
            String temp=r2[0];
            for (int i=1;i<r2.length;i++){
                temp+=(""+r2[i].charAt(0)).toUpperCase()+r2[i].substring(1,r2[i].length());
            }
            return temp;
        }
    }

}
