package com.example.sqlimporttool.importdb;

public class SingleSqlInfo {

    private String sql;

    private Object[] param;

    public SingleSqlInfo(String sql, Object[] param) {
        this.sql = sql;
        this.param = param;
    }

    public SingleSqlInfo() {

    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }


    public Object[] getParam() {
        return param;
    }

    public void setParam(Object[] param) {
        this.param = param;
    }
}
