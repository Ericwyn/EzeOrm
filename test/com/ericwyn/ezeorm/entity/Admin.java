package com.ericwyn.ezeorm.entity;

import com.ericwyn.ezeorm.annotation.AutoIncrement;
import com.ericwyn.ezeorm.annotation.Column;
import com.ericwyn.ezeorm.annotation.ColumnType;
import com.ericwyn.ezeorm.annotation.Entity;
import com.ericwyn.ezeorm.annotation.PrimaryKey;

/**
 *
 * Created by Ericwyn on 17-11-20.
 */

@Entity(table = "admin")
public class Admin {
    @PrimaryKey
    @AutoIncrement
    @Column(type = ColumnType.INT)
    private Long id;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String account;

    @Column(type = ColumnType.TEXT,notNull = true)
    private String pw;

    public Admin() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
