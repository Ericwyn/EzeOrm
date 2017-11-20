package com.ericwyn.ezeorm.dbTools;

import com.ericwyn.ezeorm.annotation.Column;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.util.List;

import test.com.ericwyn.ezeorm.entity.User;

/**
 *
 * sql语句构造器
 *
 * Created by Ericwyn on 17-11-20.
 */
public class MySQLCodeBuilder {
    public static String createTable(TableObj tableObj){
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

        System.out.println(res.toString());
        return res.toString();
    }

    public static void main(String[] args) throws Exception{
        String sqlTest=createTable(ParseTools.parseEntity(User.class));
    }
}
