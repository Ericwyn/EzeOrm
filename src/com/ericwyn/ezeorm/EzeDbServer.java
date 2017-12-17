package com.ericwyn.ezeorm;

import com.ericwyn.ezeorm.dbTools.EzeSql;
import com.ericwyn.ezeorm.dbTools.sqlbuilder.MySQLCodeBuilder;
import com.ericwyn.ezeorm.expection.EzeExpection;
import com.ericwyn.ezeorm.obj.ColumnObj;
import com.ericwyn.ezeorm.obj.TableObj;
import com.ericwyn.ezeorm.tool.EzeConfig;
import com.ericwyn.ezeorm.tool.ParseTools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * EzeOrm 最主要的类，对java绑定包含Entity映射的实体类，对数据库绑定其中的一张数据表，需要使用Builder进行新建。<br>
 *
 * q:因为对通过T获取T.class 尝试的失败，所以builder 限定了一定要通过setEntityClass 传入一个entityClass 参数
 * 如果后期能够获取到T.class 的话就可以把这个方法去掉了
 *
 * @version 1.8
 * @author Ericwyn
 * @date 17-11-20
 */
public class EzeDbServer<T> {
//    private T object;
    private Class entityClass;
    private EzeSql ezeSql;
    private Connection connection;
    private MySQLCodeBuilder coderBuilder;
    private TableObj table;

    /**
     * EzeDbServer的Builder，所有的EzeDbServer都需要用该Builder来新建，Builder需要设置如下内容<br>
     *     1.Entity标记的实体类 T<br>
     *     2.Entity标记的实体类的 Class (对通过T获取T.class 尝试的失败，所以builder 限定了一定要通过setEntityClass 传入一个entityClass 参数
     * 如果后期能够获取到T.class 的话就可以把这个方法去掉了)<br>
     *
     * @param <T> Entity标记的实体类
     */
    public static class Builder<T>{
        private Class entityClass;

        public Builder(){
//            this.entityClass=getTClass();
        }
        //因为无法通过反射获取T.class 所以需要再加上一个 setEntityClass 的方法
        public Builder<T> setEntityClass(Class<T> entityClass){
            this.entityClass=entityClass;
            return this;
        }
        private Class<T> getTClass(){
            Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return tClass;
        }

        /**
         *
         * @return 返回一个与Entity实体类绑定好的 <code>EzeDbServer</code> 对象
         * @throws EzeExpection 如果Builder 没有设置实体类对象的Class的话，那么就会抛出一个异常，无法完成创建
         */
        public EzeDbServer<T> create() throws EzeExpection{
            if(this.entityClass==null){
                try {
                    throw new EzeExpection("Builder 没有设定对象实体类，请调用 Builder.setEntityClass() 方法进行设置");
                }catch (EzeExpection e){
                    e.printStackTrace();
                }
                return null;
            }else {
                return new EzeDbServer<>(this);
            }
        }

    }

    /**
     * 私有构造方法，只能由<code>Builder</code> 的<code>create</code> 方法调用。调用时候会完成初始化，完成如下工作<br>
     *     1.根据泛型<code>T</code>生成一个<code>TableObj</code>对象<br>
     *     2.初始化 Sql 语句构建器和<code>EzeSql</code><br>
     *     3.调用<code>EzeSql</code>的初始化方法<code>initConnection()</code>
     *     （注意：由于是多个<code>EzeDBServer</code>对象共享一个<code>EzeSql</code>对象以及其中的静态变量对象，
     *     所以虽然每个<code>EzeDbServer</code>新建的时候都会调用初始化方法，但是<code>EzeSql</code>实际上只会经历一次初始化，
     *     详细可查看<code>EzeSql</code>的<code>initConnection()</code>方法）<br>
     *     4.初始化数据表（注：如果表不存在那么就会自动创建，如果存在但是表结构发生了该表，那么会删除表格后重新新建表）
     *
     * @param builder 传入一个Builder
     */
    private EzeDbServer(Builder builder) throws EzeExpection{
        this.entityClass=builder.entityClass;
        try {
            table = ParseTools.parseEntity(entityClass);
        } catch (EzeExpection ezeExpection) {
            ezeExpection.printStackTrace();
        }
        //初始化创建sql语句构建器
        coderBuilder=new MySQLCodeBuilder();
        ezeSql=new EzeSql();
        //初始化数据库Connection
        ezeSql.initConnection();
        connection=EzeSql.conn;
        //数据库连接启动的时候对表格进行检查
        //也就是会自动创建表
        initTable(table);
    }

    @Override
    public String toString() {
        return ezeSql.getUrl()+entityClass.getName();
    }


    /**
     * 通过获取表数据来判断 表是否已经创建
     * @param tableObj  TableObj对象
     * @return 如果表已经创建（存在同名的表）的情况的话，返回<code>true</code>
     */
    private boolean isTableCreate(TableObj tableObj){
        try {
            ResultSet rs  = connection.getMetaData().getTables(null, null, tableObj.getTableName() , null );
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过获取表数据来判断 表是否已经创建
     * @param tableName  TableObj对象
     * @return 如果表已经创建（存在同名的表）的情况的话，返回<code>true</code>
     */
    private boolean isTableCreate(String tableName){
        try {
            ResultSet rs  = connection.getMetaData().getTables(null, null, tableName , null );
            return rs.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过数据表的名字来获取表对象，从而来判断表结构是否有更新
     *
     * @param table_name 通过表明来获取TableObj 对象
     * @return  返回获取的TableObj
     * @throws SQLException 抛出数据库异常
     */
    private TableObj getTableObj(String table_name) {
        try {
            DatabaseMetaData  m_DBMetaData= connection.getMetaData();
            ResultSet tableRet = m_DBMetaData.getTables(null, "%",table_name,new String[]{"TABLE"});
            /*其中"%"就是表示*的意思，也就是任意所有的意思。其中m_TableName就是要获取的数据表的名字，如果想获取所有的表的名字，就可以使用"%"来作为参数了。*/
            TableObj tableObj=new TableObj();
            //3. 提取表的名字。
            while(tableRet.next()){
                if(tableRet.getString("TABLE_NAME").equals(table_name)){
                    tableObj.setTableName(table_name);
                    break;
                }
            }
            String[] db_name_temp=EzeConfig.db_connect_url.split("\\?");
            String[] db_name_temp2=db_name_temp[0].split("/");

            ResultSet primaryKeys = m_DBMetaData.getPrimaryKeys(db_name_temp2[db_name_temp2.length-1], null, table_name);
            while (primaryKeys.next()){
                tableObj.setPrimaryKey(primaryKeys.getString("COLUMN_NAME"));
            }
            ResultSet colRet = m_DBMetaData.getColumns(null,"%", table_name,"%");
            while(colRet.next()) {
                ColumnObj columnObj=new ColumnObj();
                columnObj.setName(colRet.getString("COLUMN_NAME"));
                columnObj.setType(colRet.getString("TYPE_NAME"));
                columnObj.setAutoIncrement(colRet.getBoolean("IS_AUTOINCREMENT"));
                columnObj.setNotNull(!colRet.getBoolean("NULLABLE"));
                tableObj.addColum(columnObj);
            }
            return tableObj;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 创建数据表
     * @param tableObj 传入需要创建表映射TableObj
     */
    private void createTable(TableObj tableObj){
        ezeSql.runSQL(coderBuilder.createTable(tableObj));
        System.out.println("成功创建"+tableObj.getTableName()+"表");
    }

    /**
     * 判断表格是否发生了变化
     * @param table 表映射TableObj对象
     * @return 如果发生了变化那么就返回<code>true</code>
     */
    private boolean isTableChange(TableObj table){
        TableObj oldTableObj=getTableObj(table.getTableName());
        if(table.equals(oldTableObj)){
            System.out.println(table.getTableName()+"表结构未改变");
            return false;
        }else {
            System.out.println(table.getTableName()+"表结构与数据库表对比发生了变化");
            return true;
        }
    }

    //

    /**
     * 同名数据表存在的情况下，判断现有的表对象是否和数据库当中的表兼容。<br>
     * 所谓兼容指的是，当前所有的字段都能够与表总的字段找到对应关系。
     *
     * @param table 运行过程中由Entity注解后解析得到的表TableObj
     * @return  如果可以兼容的话返回<code>true</code>
     */
    private boolean isTableCompatible(TableObj table){
        return true;
    }


    /**
     *  更新表的结构方法，当前仅有一种处理方式
     *      1.重命名旧的数据表，而后依据新的TableObj对象建立新的数据表
     *
     * @param table 当前运行中解析得到的最新表结构映射 TableObj对象
     */
    private void updateTableStruc(TableObj table) throws EzeExpection{
        switch (EzeConfig.db_update_model) {
            case "no":
                throw new EzeExpection("表结构已更新但是未进行处理");
            case "backup":
                //新的表结构更新处理方式，重命名旧的表格之后，创建一个新的表格
                TableObj oldTable = getTableObj(table.getTableName());
                String newTableName = oldTable.getTableName() + "x";
                //备份的表格将重命名为 "旧表名x"
                while (isTableCreate(newTableName)) {
                    newTableName = newTableName + "x";
                }
                renameTable(oldTable, newTableName);
                createTable(table);
                break;
            default:
                throw new EzeExpection("表结构更新模式未设置");
        }
        //旧的表结构更新处理方法，直接删除之后新建一个新的表
//        dropTable();
//        createTable(table);
    }

    /**
     * 重命名数据表，该方法私有，用户无法使用EzeOrm 的方法完成对数据表的修改，因为那样将很有可能出现错误
     *
     * @param tableObj 需要修改名称的数据表
     * @param newTableName  数据表的新名称
     */
    private void renameTable(TableObj tableObj,String newTableName){
        ezeSql.runSQL(coderBuilder.renameTable(tableObj,newTableName));
        System.out.println("旧的数据表 "+tableObj.getTableName()+" 已重命名为:"+newTableName);
    }



    /**
     * 数据库连接启动的时候对表格进行检查，步骤如下<br>
     *  1.判断表格是否存在，不存在的话创建表格<br>
     *  2.如果表格存在，判断表格是否有变化<br>
     *  3.如果表结构有变化，调用表结构更新方法<br>
     *
     * @param table 由实体类对象反射解析所得的表结构
     *
     */
    private void initTable(TableObj table) throws EzeExpection{
        //判断表是否存在
        if(isTableCreate(table)){
            //已经存在，判断是否改变,如果改变的话 ，更新表结构
            if(isTableChange(table)){
                updateTableStruc(table);
            }
        }else {
            //不存在
            createTable(table);
        }
    }

    /**
     * 查找数据库中所有的数据
     * @return  返回一个List，包含解析过的所有数据
     */
    public List<T> findAll(){
        ResultSet resultSet = ezeSql.runSQLForRes(coderBuilder.findAll(table));
        return parseResultSet(resultSet);
    }

    /**
     * 按照条件对数据进行查找
     * @param attributes 条件语句,就是 SELECT 语句后面 WHERE 条件句
     * @return  返回一个List，包含解析过的所有数据
     */
    public List<T> findByAttributes(String... attributes){
        ResultSet resultSet = ezeSql.runSQLForRes(coderBuilder.findByAttributes(table,attributes));
        return parseResultSet(resultSet);
    }

    /**
     * 插入数据
     * @param t 需要插入的对象
     */
    public void insert(T t){
        ezeSql.runSQL(coderBuilder.insert(table,t));
    }


    /**
     * 插入多条数据
     * @param list 需要插入的数据列
     */
    public void insertList(List<T> list){
        for (T temp:list){
            insert(temp);
        }
    }

    /**
     * 删除一条数据
     * @param t 需要删除的数据对象
     */
    public void delete(T t){
        ezeSql.runSQL(coderBuilder.delete(table,t));
    }

    /**
     * 删除数据库中全部数据，表结构保留
     */
    public void deleteAll(){
        ezeSql.runSQL(coderBuilder.deleteAll(table));
    }

    /**
     * 通过条件删除数据
     * @param attributes 条件语句,就是 DELETE 语句后面 WHERE 条件句
     */
    public void deleteByAttributes(String... attributes){
        ezeSql.runSQL(coderBuilder.deleteByAttributes(table,attributes));
    }

    /**
     * 删除多条数据
     * @param list 需要删除的数据列
     */
    public void deleteList(List<T> list){
        for (T temp:list){
            delete(temp);
        }
    }

    /**
     * 删除数据表，包括全部数据和表结构
     */
    public void dropTable(){
        ezeSql.runSQL(coderBuilder.dropTable(table));
    }

    /**
     * 更新数据
     * @param t 需要更新的数据对象
     */
    public void update(T t){
        ezeSql.runSQL(coderBuilder.update(table,t));
    }

    /**
     * 保留的运行sql语句的方法，返回的是一个<code>ResultSet</code> ，适用于运行一些级联查询之类的语句
     *
     * @param sqlCode   需要运行的sql语句
     * @return  jdbc运行sql语句后返回的rs
     */
    public ResultSet runQueryForRes(String sqlCode){
        return ezeSql.runSQLForRes(sqlCode);
    }

    /**
     * 保留的运行sql语句的方法，无返回值
     * @param sqlCode 需要运行的sql语句
     */
    public void runQuery(String sqlCode){
        ezeSql.runSQL(sqlCode);
    }

    /**
     * 通过反射实现的 <code>ResultSet</code>  解析方法
     * @param resultSet 通过jdbc 运行后的到<code>ResultSet</code>对象
     * @return 返回解析后的对象列表
     */
    public List<T> parseResultSet(ResultSet resultSet){
        List<T> list=new ArrayList<>();
        if(resultSet!=null){
            try {
                Class clazz =entityClass;
                Method[] methods = clazz.getMethods();
                List<ColumnObj> columns = table.getColumns();
                Field[] fields=clazz.getFields();
                String methodNameTemp;
                //获取每一列
                while (resultSet.next()){
                    Object object = clazz.newInstance();
                    //通过字段名字解析每一列
                    //遍历所有的字段，取得字段的名字
                    for (ColumnObj columnObj:columns){
                        String fieldName=ParseTools.getFieldNameFormColumnName(columnObj.getName());
                        String classOfField=entityClass.getDeclaredField(fieldName).getType().getName();
                        Class ca=entityClass.getDeclaredField(fieldName).getClass();
                        switch (columnObj.getType()){
                            //通过类型来获取变量，而后将变量塞到对应的object里面，再把object塞到list里面！！！！！！！！！！！就是这样的
                            //INT 代表 int、Integer、byte、Byte、short、Short、long、Long、Boolean、boolean
                            case "INT":
                                for (Method method:methods){
                                    if(method.getParameterCount()==0){
                                        continue;
                                    }
                                    methodNameTemp=method.getName().toLowerCase();
                                    if(methodNameTemp.contains("set")
                                            && methodNameTemp.contains(columnObj.getName().replaceAll("_","").toLowerCase())){
                                        if (classOfField.contains("Long") || classOfField.contains("long")){
                                            method.invoke(object,(long)resultSet.getLong(columnObj.getName()));
                                        }
                                        else if (classOfField.contains("Integer") || classOfField.contains("int")){
                                            method.invoke(object,(int)resultSet.getLong(columnObj.getName()));
                                        }
                                        else if (classOfField.contains("Byte") || classOfField.contains("byte")){
                                            method.invoke(object,(byte)resultSet.getLong(columnObj.getName()));
                                        }
                                        else if (classOfField.contains("Short") || classOfField.contains("short")){
                                            method.invoke(object,(short)resultSet.getLong(columnObj.getName()));
                                        }
                                        else if (classOfField.contains("Boolean") || classOfField.contains("boolean")){
                                            if((int)resultSet.getLong(columnObj.getName())==1){
                                                method.invoke(object,true);
                                            }else {
                                                method.invoke(object,false);
                                            }
                                        }
                                        break;
                                    }
                                }
                                break;
                            case "DOUBLE":
                                for (Method method:methods){
                                    if(method.getParameterCount()==0){
                                        continue;
                                    }
                                    methodNameTemp=method.getName().toLowerCase();
                                    if(methodNameTemp.contains("set")
                                            && methodNameTemp.contains(columnObj.getName().replaceAll("_","").toLowerCase())){
                                        method.invoke(object, (double)resultSet.getDouble(columnObj.getName()));
                                        break;
                                    }
                                }
                                break;
                            case "BIGINT":

                                for (Method method:methods){
                                    if(method.getParameterCount()==0){
                                        continue;
                                    }
                                    methodNameTemp=method.getName().toLowerCase();
                                    if(methodNameTemp.contains("set")
                                            && methodNameTemp.contains(columnObj.getName().replaceAll("_","").toLowerCase())){
                                        method.invoke(object, (long)resultSet.getLong(columnObj.getName()));
                                        break;
                                    }
                                }
                                break;
                            case "TEXT":
                                for (Method method:methods){
                                    if(method.getParameterCount()==0){
                                        continue;
                                    }
                                    methodNameTemp=method.getName().toLowerCase();
                                    if(methodNameTemp.contains("set")
                                            && methodNameTemp.contains(columnObj.getName().replaceAll("_","").toLowerCase())){
                                        method.invoke(object, (String)resultSet.getString(columnObj.getName()));
                                        break;
                                    }
                                }
                                break;
                            case "DATETIME":
                                for (Method method:methods){
                                    if(method.getParameterCount()==0){
                                        continue;
                                    }
                                    methodNameTemp=method.getName().toLowerCase();
                                    if(methodNameTemp.contains("set")
                                            && methodNameTemp.contains(columnObj.getName().replaceAll("_","").toLowerCase())){
                                        method.invoke(object,(Date)resultSet.getTimestamp(columnObj.getName()));
                                        break;
                                    }
                                }
                                break;
                        }
                    }
                    list.add((T)object);
                }
                return list;
            } catch (SQLException
                    | IllegalAccessException
                    | NoSuchFieldException
                    | InstantiationException
                    | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }else {
            return null;
        }
    }

}
