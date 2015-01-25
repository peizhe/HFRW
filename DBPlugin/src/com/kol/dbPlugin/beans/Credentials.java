package com.kol.dbPlugin.beans;

public class Credentials {

    private boolean exist;
    private String host;
    private String username;
    private String password;

    public Credentials() {
        this.exist = false;
    }

    public Credentials(String host, String username, String password) {
        this.exist = true;
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public boolean isExist() {
        return exist;
    }

    @Override
    public String toString() {
        return "host=" + host + ", username=" + username + ", password=" + password;
    }
}