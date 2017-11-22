package com.ericwyn.ezeorm.tool;

import com.ericwyn.ezeorm.annotation.AutoIncrement;
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
 * Created by Ericwyn on 17-11-20.
 */
public class ParseTools {

    public static final SimpleDateFormat sdfForDATATIME=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 解析注解的工具
     * @param cla   映射对象
     * @return  返回一个 Table 对象
     * @throws EzeExpection
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
                            //分割驼峰命名法
                            //通过统一的逻辑由类属性名称获取字段的命名。
//                            columnObj.setName(column.name());
                            String[] r = fieldNameTemp.split("(?=[A-Z])");
                            if(r.length==0){
                                columnObj.setName(r[0].toLowerCase());
                            }else {
                                String temp="";
                                for (int i=0;i<r.length;i++){
                                    if (i!=r.length-1){
                                        temp+=r[i].toLowerCase()+"_";
                                    }else {
                                        temp+=r[i].toLowerCase();
                                    }
                                }
                                columnObj.setName(temp);
                            }
                            columnObj.setNotNull(column.notNull());
                            columnObj.setType(column.type().toString());
                        }
                        if(annotation instanceof PrimaryKey){
                            PrimaryKey primaryKey=(PrimaryKey)annotation;
                            tableObj.setPrimaryKey(field.getName());
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

}
