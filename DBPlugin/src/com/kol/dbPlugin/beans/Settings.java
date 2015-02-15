package com.kol.dbPlugin.beans;

import com.kol.dbPlugin.interfaces.Property;
import org.jetbrains.annotations.NotNull;

public class Settings implements Property<Settings> {

    private String host;
    private String port;
    private boolean exist;
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

    public Settings(DBConnectInfo info) {
        this.exist = true;
        this.host = info.getHost();
        this.database = info.getDatabase();
    }

    @Override
    public boolean exist() {
        return exist;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void update(@NotNull Settings data) {
        host = data.getHost();
        port = data.getPort();
        database = data.getDatabase();
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return "host=" + host + ", port=" + port + ", database=" + database;
    }

    public static Settings placeholder(@NotNull final String host, @NotNull final String dbName){
        return new Settings(host, null, dbName);
    }
}