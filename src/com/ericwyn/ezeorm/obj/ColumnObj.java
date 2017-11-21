package com.ericwyn.ezeorm.obj;


import java.util.List;

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

//    /**
//     * 判断这个属性是不是在另一个表里也存在
//     *
//     * @param table
//     * @return
//     */
//    public boolean inAnotherTable(TableObj table){
//        List<ColumnObj> columns = table.getColumns();
//        boolean resFlag=false;
//        for (ColumnObj columnObj:table.getColumns()){
//            if(this.getName().equals(columnObj.getName())){
//                if(this.getType().equals(columnObj.getType())){
//                    if(this.isNotNull()==columnObj.isNotNull()){
//                        if(this.isAutoIncrement()==columnObj.isAutoIncrement()){
//                            resFlag=true;
//                            break;
//                        }else {
//                            System.out.println("列非空不同");
//                            resFlag=false;
//                        }
//                    }else {
//                        System.out.println("列自增加不同");
//                        resFlag=false;
//                    }
//                }else {
//                    System.out.println("列类型不同");
//                    resFlag=false;
//                }
//            }else {
//                System.out.println("列名字不同");
//                resFlag=false;
//            }
//        }
//        return resFlag;
//    }

}
