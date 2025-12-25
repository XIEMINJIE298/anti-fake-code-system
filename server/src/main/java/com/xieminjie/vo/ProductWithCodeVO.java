package com.xieminjie.vo;

import com.alibaba.excel.annotation.ExcelProperty;

public class ProductWithCodeVO {
    @ExcelProperty("产品序列号")
    private String serialNumber;

    @ExcelProperty("产品名称")
    private String productName;

    @ExcelProperty("产品类别")
    private String category;

    @ExcelProperty("产品型号")
    private String model;

    @ExcelProperty("生产批次")
    private String batchNumber;

    @ExcelProperty("生产厂家")
    private String manufacturer;

    @ExcelProperty("防伪码")
    private String antiFakeCode;

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

    public String getAntiFakeCode() {
        return antiFakeCode;
    }

    public void setAntiFakeCode(String antiFakeCode) {
        this.antiFakeCode = antiFakeCode;
    }
}
