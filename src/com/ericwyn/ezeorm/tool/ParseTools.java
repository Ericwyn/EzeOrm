package com.ericwyn.ezeorm.tool;

import com.ericwyn.ezeorm.annotation.AutoIncrement;
import com.ericwyn.ezeorm.annotation.PrimaryKey;
import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.annotation.Column;
import com.ericwyn.ezeorm.annotation.Entity;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

import test.com.ericwyn.ezeorm.entity.User;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class ParseTools {

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

            Field[] fields = classA.getDeclaredFields();
            for (Field field:fields){
                Annotation[] annotationsOfField=field.getAnnotations();
                ColumnObj columnObj=new ColumnObj();
                boolean isColumn=false;

                for (Annotation annotation:annotationsOfField){
                    if (annotation instanceof Column){
                        Column column=(Column) annotation;
                        isColumn=true;
                        columnObj.setName(column.name());
                        columnObj.setNotNull(column.notNull());
                        columnObj.setType(column.type().toString());
                    }
                    if(annotation instanceof PrimaryKey){
                        PrimaryKey primaryKey=(PrimaryKey)annotation;
                        tableObj.setPrimaryKey(field.getName());
                    }
                    if(annotation instanceof AutoIncrement){
                        AutoIncrement autoIncrement=(AutoIncrement)annotation;
                        columnObj.setAutoIncrement(true);
                    }
                }

                if(isColumn){
                    //只有确认了这个是参数才会添加
                    columns.add(columnObj);
                }

            }
            tableObj.setColumns(columns);
            return tableObj;
        }
    }

    public static void main(String[] args) throws Exception {

        TableObj tableObj =parseEntity(User.class);
        System.out.println(tableObj);

    }
}
