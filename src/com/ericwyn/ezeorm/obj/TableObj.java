package com.ericwyn.ezeorm.obj;

import java.util.List;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class TableObj {
    private String tableName;
    List<ColumnObj> columns;
    private String primaryKey;
    public TableObj() {

    }

    public TableObj(String tableName) {
        this.tableName = tableName;
    }

    public TableObj(String tableName, List<ColumnObj> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnObj> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnObj> columns) {
        this.columns = columns;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
