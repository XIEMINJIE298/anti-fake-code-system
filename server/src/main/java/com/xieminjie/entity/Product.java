package com.xieminjie.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 产品信息主表(Product)实体类
 *
 * @author makejava
 * @since 2025-12-22 17:53:00
 */
public class Product implements Serializable {
    private static final long serialVersionUID = -48560565850609599L;
/**
     * 主键ID
     */
    private Long id;
/**
     * 产品序列号
     */
    private String serialNumber;
/**
     * 产品名称
     */
    private String productName;
/**
     * 产品类别
     */
    private String category;
/**
     * 产品型号
     */
    private String model;
/**
     * 生产批次
     */
    private String batchNumber;
/**
     * 生产厂家
     */
    private String manufacturer;
/**
     * 创建时间
     */
    private Date createdTime;
/**
     * 更新时间
     */
    private Date updatedTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

}

