package com.kol.dbPlugin;

public class Credentials {

    private String server;
    private String username;
    private String password;

    public Credentials(String server, String username, String password) {
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "username=" + username + "\npassword=" + password;
    }
}