package com.ericwyn.ezeorm.obj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EzeOrm 的主要对象之一，该对象是对数据表的描述与映射，包含如下信息<br>
 *     1.表名<br>
 *     2.字段数据<code> List<ColumnObj> </code><br>
 *     3.主键数据<code> List<String> </code><br>
 * 另外还包含重写的方法
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
public class TableObj {
    private String tableName;
    List<ColumnObj> columns;
    private List<String> primaryKey=new ArrayList<>();
    public TableObj() {

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

    /**
     * 得到所有的主键名称，用已完成表格的创建<br>
     *
     * @return 返回主键名称，多个主键之间使用<code>,</code>进行分割
     */
    public String getPrimaryKeyName() {
        StringBuilder temp = new StringBuilder();
        for (int i=0;i<primaryKey.size();i++){
            if (i!=primaryKey.size()-1){
                temp.append(primaryKey.get(i)).append(",");
            }else{
                temp.append(primaryKey.get(i));
            }
        }
        return temp.toString();
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey.add(primaryKey);
    }

    /**
     * 像TableObj对象增加字段属性
     * @param columnObjs    可传入多个字段属性
     */
    public void addColum(ColumnObj... columnObjs){
        if(this.columns==null){
            this.columns=new ArrayList<>();
        }
        this.columns.addAll(Arrays.asList(columnObjs));
    }

    /**
     * 重写的equals方法，通过这个方法来判断TableObj是否相同，
     * 以此完成对表格是否存在以及表结构是否更改的判断，对比步骤具体如下。<br>
     *     1.判断表格名称、表主键数量、表字段数量是否相等<br>
     *     2.对每个表主键都进行对比判断<br>
     *     3.对每个属性字段都进行对比<br>
     *
     * @param obj 传入需要进行比较的TableObj 对象
     * @return 如果两个表对象相等，返回<code>true</code>
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TableObj){
            if(((TableObj) obj).getTableName().equals(this.getTableName())
                    && ((TableObj) obj).getPrimaryKey().size() == this.getPrimaryKey().size()
//                    && ((TableObj) obj).getPrimaryKey().equals(this.getPrimaryKey())
                    && ((TableObj) obj).getColumns().size()== this.getColumns().size()){
                boolean primaryKeyEqualsFlag=false;
                for (String objPrimary:((TableObj) obj).getPrimaryKey()){
                    //每次开始都先认定这个key不存在
                    primaryKeyEqualsFlag=false;
                    //判断这个key是否存在
                    for (String thisPrimary:this.getPrimaryKey()){
                        primaryKeyEqualsFlag=true;
                        break;
                    }
                    //如果这个key的都不存在的话就已经不用判断了
                    if(!primaryKeyEqualsFlag){
                        break;
                    }
                }
                if(!primaryKeyEqualsFlag){
                    return false;
                }
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

    /**
     * 重写的HashCode 方法
     * @return 返回hashCode
     */
    @Override
    public int hashCode() {
        return this.getTableName().length()
                +this.getPrimaryKey().size()
                +this.getColumns().size();
    }

}
