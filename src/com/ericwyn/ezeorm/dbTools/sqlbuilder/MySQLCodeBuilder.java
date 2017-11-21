package com.ericwyn.ezeorm.dbTools.sqlbuilder;

import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.util.List;

/**
 *
 * sql语句构造器
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
}
