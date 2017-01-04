package com.jsonModel;

/**
 * Created by takahiro on 2016/12/28.
 */
public class FromSqlSubmit {
    private int id;
    private String username;
    private String sql;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
