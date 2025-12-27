package com.xieminjie.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (TUser)实体类
 *
 * @author makejava
 * @since 2025-12-26 10:31:35
 */
public class TUser implements Serializable {
    private static final long serialVersionUID = 275573893466138078L;

    private Long id;

    private String username;

    private String password;

    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}

