package com.kol.dbPlugin.beans;

import com.kol.dbPlugin.C;

import java.nio.file.Path;

public class DBConnectInfo {

    private String host;
    private String database;
    private Path projectPath;

    public DBConnectInfo(String host, String database, Path projectPath) {
        this.host = host;
        this.database = database;
        this.projectPath = projectPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return projectPath.toString() + ": " + host + "/" + database;
    }

    public Path path() {
        return projectPath.resolve(C.PLUGIN_DIRECTORY_NAME).resolve(host).resolve(database);
    }
}
