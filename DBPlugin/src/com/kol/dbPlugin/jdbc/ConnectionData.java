package com.kol.dbPlugin.jdbc;

public class ConnectionData {
    private String url;
    private String user;
    private String password;
    private String driverName;

    public ConnectionData(String url, String user, String password, String driverName) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driverName = driverName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
