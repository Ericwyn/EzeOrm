package com.ericwyn.ezeorm.entity;

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
    @Column(type = ColumnType.INT)
    private Long idd;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String name;

    @Column(type = ColumnType.INT,notNull = true)
    private int age;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String sex;

    @Column(type = ColumnType.DATETIME,notNull = true)
    private Date timeStamp;

    @Column(type = ColumnType.INT)
    private boolean good;

    public User() {

    }

    public Long getIdd() {
        return idd;
    }

    public void setIdd(Long idd) {
        this.idd = idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

}
