package com.kol.dbPlugin.beans;

public class Settings {

    private boolean exist;

    private String host;
    private String port;
    private String database;

    public Settings() {
        this.exist = false;
    }

    public Settings(String host, String port, String database) {
        this.host = host;
        this.port = port;
        this.exist = true;
        this.database = database;
    }

    public boolean isExist() {
        return exist;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }
}