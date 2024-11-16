package com.example.chat.bean;

import java.io.Serializable;

/**
 * 联系人
 */
public class Contacts implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer toUserId;//所属人

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }
    public Contacts(Integer id, Integer userId, Integer toUserId) {
        this.id = id;
        this.userId = userId;
        this.toUserId = toUserId;
    }
}
