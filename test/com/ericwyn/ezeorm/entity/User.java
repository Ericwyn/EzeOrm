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
    private Long id;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String name;

    @Column(type = ColumnType.INT,notNull = true)
    private int age;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String sex;

    @Column(type = ColumnType.DATE,notNull = true)
    private Date registerDate;

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDate() {
        return registerDate;
    }

    public void setDate(Date date) {
        this.registerDate = date;
    }
}
