package com.ericwyn.ezeorm.obj;


import java.util.List;

/**
 *
 * 数据表对象映射类。<br>
 * 包含了该字段的名称、类型、非空表示、自增标识<br>
 *     EzeOrm认为主键标识是属于数据表的而非字段，所以该类中不包含主键标识
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
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

    /**
     * 判断这个字段是否与另一个字段相同，判断步骤如下<br>
     *     1.判断字段名称<br>
     *     2.判断字段类型<br>
     *     3.判断字段非空标识<br>
     *     4.判断自增表示<br>
     *
     * @param obj 传入需要一个与之比较的列表对象
     * @return 相等的话返回<code>true</code>
     */
    @Override
    public boolean equals(Object obj) {
        if(((ColumnObj) obj).getName().equals(this.getName())){
            if(((ColumnObj) obj).getType().equals(this.getType())){
                if(((ColumnObj) obj).isNotNull()==this.isNotNull()){
                    if (((ColumnObj) obj).isAutoIncrement()==this.isAutoIncrement()){
                        return true;
                    }else {
                        return false;
                    }
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }else {
            return false;
        }

    }

}
