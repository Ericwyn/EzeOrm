package com.ericwyn.ezeorm.obj;

import java.util.ArrayList;
import java.util.Arrays;
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
        if(this.primaryKey==null || this.primaryKey.equals("")){
            this.primaryKey = primaryKey;
        }else {
            this.primaryKey=this.primaryKey+","+primaryKey;
        }

    }

    public void addColum(ColumnObj... columnObjs){
        if(this.columns==null){
            this.columns=new ArrayList<>();
        }
        this.columns.addAll(Arrays.asList(columnObjs));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TableObj){
            if(((TableObj) obj).getTableName().equals(this.getTableName())
                    && ((TableObj) obj).getPrimaryKey().equals(this.getPrimaryKey())
                    && ((TableObj) obj).getColumns().size()== this.getColumns().size()){

                boolean columnEqualsFlag=true;
                for (ColumnObj columnObj:((TableObj) obj).getColumns()){
                    if(!this.getColumns().contains(columnObj)){
//                            System.out.println("表格属性不同");
                        columnEqualsFlag=false;
                        break;
                    }
                }
                return columnEqualsFlag;
            }else {
//                System.out.println("表格名字不同");
                return false;
            }
        }else {
//            System.out.println("表格不是同个类型");
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getTableName().length()
                +this.getPrimaryKey().length()
                +this.getColumns().size();
    }

//    //与另个表对比是否不一样
//    public boolean equalsAnotherTable(TableObj obj){
//        if(this.getTableName().equals(obj.getTableName())){
//            if(this.getColumns().size()==obj.getColumns().size()){
//                if(this.getPrimaryKey().equals(obj.getPrimaryKey())){
//                    for (ColumnObj columnObj:this.getColumns()){
//                        if(!columnObj.inAnotherTable(obj)){
//                            System.out.println(columnObj.getName()+"不在另一个表里面");
//                            return false;
//                        }
//                    }
//                    return true;
//                }else {
//                    System.out.println("主键 不同");
//                    return false;
//                }
//            }else {
//                System.out.println("列数量不同");
//                return false;
//            }
//        }else {
//            System.out.println("表明不同");
//            return false;
//        }
//    }


}
