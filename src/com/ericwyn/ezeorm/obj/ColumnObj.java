package com.ericwyn.ezeorm.obj;

/**
 * Created by Ericwyn on 17-11-20.
 */
public class ColumnObj {
    private String name;
    private String type;
    private boolean notNull ;

    public ColumnObj(String name, String type, boolean notNull) {
        this.name = name;
        this.type = type;
        this.notNull = notNull;
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
}
