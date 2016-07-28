package com.lyxxcy.db;

import java.io.Serializable;

public class UserInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static long FLAG_ADMIN = 0x0001;

    private String name;
    private String password;
    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserInfo [name=" + name + ", password=" + password + ",id=" + id + "]";
    }

}
