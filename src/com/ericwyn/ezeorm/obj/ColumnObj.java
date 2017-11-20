package com.ericwyn.ezeorm.obj;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class ColumnObj {
    private String name;
    private String type;
    private boolean notNull ;       //是否非空
    private boolean autoIncrement;  //是否自动递增

    public ColumnObj() {
        notNull=false;
        autoIncrement=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
