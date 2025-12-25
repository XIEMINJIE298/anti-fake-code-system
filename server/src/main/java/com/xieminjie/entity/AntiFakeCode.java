package com.xieminjie.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 防伪码表(AntiFakeCode)实体类
 *
 * @author makejava
 * @since 2025-12-22 17:52:59
 */
public class AntiFakeCode implements Serializable {
    private static final long serialVersionUID = -23436535187941692L;
/**
     * 主键ID
     */
    private Long id;
/**
     * 防伪码
     */
    private String code;
/**
     * 产品序列号
     */
    private String serialNumber;
/**
     * 产品ID
     */
    private Long productId;
/**
     * 状态: 1-有效 0-已作废
     */
    private Integer status;
/**
     * 生成时间
     */
    private Date generateTime;
/**
     * 首次验证时间
     */
    private Date firstVerifyTime;
/**
     * 累计验证次数
     */
    private Integer verifyCount;
/**
     * 创建时间
     */
    private Date createdTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public Date getFirstVerifyTime() {
        return firstVerifyTime;
    }

    public void setFirstVerifyTime(Date firstVerifyTime) {
        this.firstVerifyTime = firstVerifyTime;
    }

    public Integer getVerifyCount() {
        return verifyCount;
    }

    public void setVerifyCount(Integer verifyCount) {
        this.verifyCount = verifyCount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

}

