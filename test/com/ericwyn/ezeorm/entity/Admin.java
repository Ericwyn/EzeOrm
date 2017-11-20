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
    private Long id;

    @Column(name = "name",type = ColumnType.TEXT)
    private String name;

    public Admin() {

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
}
