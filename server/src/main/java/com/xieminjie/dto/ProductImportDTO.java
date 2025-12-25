package com.xieminjie.dto;

import com.alibaba.excel.annotation.ExcelProperty;

public class ProductImportDTO {
    /**
     * 产品序列号
     */
    @ExcelProperty(value = "产品序列号", index = 0)
    private String serialNumber;

    /**
     * 产品名称
     */
    @ExcelProperty(value = "产品名称", index = 1)
    private String productName;

    /**
     * 产品类别
     */
    @ExcelProperty(value = "产品类别", index = 2)
    private String category;

    /**
     * 产品型号
     */
    @ExcelProperty(value = "产品型号", index = 3)
    private String model;

    /**
     * 生产批次
     */
    @ExcelProperty(value = "生产批次", index = 4)
    private String batchNumber;

    /**
     * 生产厂家
     */
    @ExcelProperty(value = "生产厂家", index = 5)
    private String manufacturer;

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
}
