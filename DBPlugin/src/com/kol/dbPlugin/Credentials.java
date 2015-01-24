package com.kol.dbPlugin;

public class Credentials {

    private boolean exist;
    private String server;
    private String username;
    private String password;

    public Credentials() {
        this.exist = false;
    }

    public Credentials(String server, String username, String password) {
        this.exist = true;
        this.server = server;
        this.username = username;
        this.password = password;
    }

    public Credentials(String username, String password) {
        this.server = "general";
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getServer() {
        return server;
    }

    public boolean isExist() {
        return exist;
    }

    @Override
    public String toString() {
        return "server=" + server + ", username=" + username + ", password=" + password;
    }
}