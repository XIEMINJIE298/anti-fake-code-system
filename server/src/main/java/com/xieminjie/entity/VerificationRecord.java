package com.xieminjie.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 防伪码验证记录表(VerificationRecord)实体类
 *
 * @author makejava
 * @since 2025-12-22 17:53:00
 */
public class VerificationRecord implements Serializable {
    private static final long serialVersionUID = 302521413760028577L;
/**
     * 主键ID
     */
    private Long id;
/**
     * 防伪码
     */
    private String antiFakeCode;
/**
     * 产品ID
     */
    private Long productId;
/**
     * 验证IP地址
     */
    private String verifyIp;
/**
     * 验证时间
     */
    private Date verifyTime;
/**
     * 用户代理信息
     */
    private String userAgent;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAntiFakeCode() {
        return antiFakeCode;
    }

    public void setAntiFakeCode(String antiFakeCode) {
        this.antiFakeCode = antiFakeCode;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getVerifyIp() {
        return verifyIp;
    }

    public void setVerifyIp(String verifyIp) {
        this.verifyIp = verifyIp;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}

