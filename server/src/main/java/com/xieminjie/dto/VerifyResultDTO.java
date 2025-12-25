package com.xieminjie.dto;

import java.util.Date;

public class VerifyResultDTO {
    private boolean valid; // 是否有效
    private String message; // 验证结果信息
    private String serialNumber; // 产品序列号
    private String productName; // 产品名称
    private String category; // 产品类别
    private String model; // 产品型号
    private String batchNumber; // 生产批次
    private String manufacturer; // 生产厂家
    private Integer verifyCount; // 验证次数
    private Date firstVerifyTime; // 首次验证时间
    private Date currentVerifyTime; // 当前验证时间

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Integer getVerifyCount() {
        return verifyCount;
    }

    public void setVerifyCount(Integer verifyCount) {
        this.verifyCount = verifyCount;
    }

    public Date getFirstVerifyTime() {
        return firstVerifyTime;
    }

    public void setFirstVerifyTime(Date firstVerifyTime) {
        this.firstVerifyTime = firstVerifyTime;
    }

    public Date getCurrentVerifyTime() {
        return currentVerifyTime;
    }

    public void setCurrentVerifyTime(Date currentVerifyTime) {
        this.currentVerifyTime = currentVerifyTime;
    }
}