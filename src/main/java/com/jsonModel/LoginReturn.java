package com.jsonModel;

/**
 * Created by Jack on 2016/12/28.
 */
public class LoginReturn {
    int id;
    String name;
    String className;
    String idty;
    String email;
    String status;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setIdty(String idty) {
        this.idty = idty;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getIdty() {
        return idty;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
