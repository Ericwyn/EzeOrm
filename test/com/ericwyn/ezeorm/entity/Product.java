package com.ericwyn.ezeorm.entity;

import com.ericwyn.ezeorm.annotation.AutoIncrement;
import com.ericwyn.ezeorm.annotation.Column;
import com.ericwyn.ezeorm.annotation.ColumnType;
import com.ericwyn.ezeorm.annotation.Entity;
import com.ericwyn.ezeorm.annotation.PrimaryKey;

/**
 * Created by Ericwyn on 17-12-2.
 */
@Entity(table = "product")
public class Product {
    @PrimaryKey
    @AutoIncrement
    @Column(type = ColumnType.INT)
    private int id;

    //商品名称
    @Column(type = ColumnType.TEXT,notNull = true)
    private String name;

    //商品进价
    @Column(type = ColumnType.DOUBLE,notNull = true)
    private Double primePrice;

    //商品售价
    @Column(type = ColumnType.DOUBLE,notNull = true)
    private Double expectedPrice;

    //商品库存
    @Column(type = ColumnType.INT,notNull = true)
    private int productNum;

    //商品编号
    @PrimaryKey
    @Column(type = ColumnType.BIGINT,notNull = true)
    private long productId;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Product() {
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrimePrice() {
        return primePrice;
    }

    public void setPrimePrice(Double primePrice) {
        this.primePrice = primePrice;
    }

    public Double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(Double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }
}
