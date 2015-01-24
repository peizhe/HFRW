package com.kol.dbPlugin;

public class Settings {

    private boolean exist;

    private String url;
    private String port;
    private String server;
    private String database;

    public Settings() {
        this.exist = false;
    }

    public Settings(String server, String url, String port, String database) {
        this.url = url;
        this.port = port;
        this.exist = true;
        this.server = server;
        this.database = database;
    }

    public boolean isExist() {
        return exist;
    }

    public String getUrl() {
        return url;
    }

    public String getPort() {
        return port;
    }

    public String getServer() {
        return server;
    }

    public String getDatabase() {
        return database;
    }
}