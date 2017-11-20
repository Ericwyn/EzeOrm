package test.com.ericwyn.ezeorm.entity;

import com.ericwyn.ezeorm.annotation.AutoIncrement;
import com.ericwyn.ezeorm.annotation.Column;
import com.ericwyn.ezeorm.annotation.ColumnType;
import com.ericwyn.ezeorm.annotation.Entity;
import com.ericwyn.ezeorm.annotation.PrimaryKey;

import java.util.Date;


/**
 *
 * user的实体类
 * Created by Ericwyn on 17-11-20.
 */
@Entity(table = "user")
public class User {

    @PrimaryKey
    @AutoIncrement
    @Column(name = "id",type = ColumnType.INT)
    private Long id;

    @Column(name = "name",type = ColumnType.TEXT,notNull = true)
    private String name;

    @Column(name = "age",type = ColumnType.INT,notNull = true)
    private int age;

    @Column(name = "sex",type = ColumnType.TEXT,notNull = true)
    private String sex;

    @Column(name = "registerDate",type = ColumnType.DATE,notNull = true)
    private Date date;

}
