package com.kol.dbPlugin.beans;

import com.google.gson.JsonObject;
import com.kol.dbPlugin.interfaces.Property;
import org.jetbrains.annotations.NotNull;

public class Credentials implements Property<Credentials> {

    private String host;
    private boolean exist;
    private String username;
    private String password;

    public Credentials() {
        this.exist = false;
    }

    public Credentials(String host, String username, String password) {
        this.host = host;
        this.exist = true;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @NotNull
    @Override
    public JsonObject toGson() {
        final JsonObject gson = new JsonObject();
        gson.addProperty("host", host);
        gson.addProperty("username", username);
        gson.addProperty("password", password);
        return gson;
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
    public void update(@NotNull Credentials data) {
        username = data.getUsername();
        password = data.getPassword();
    }

    @Override
    public String toString() {
        return "host=" + host + ", username=" + username + ", password=" + password;
    }

    public static Credentials placeholder(@NotNull final String host){
        return new Credentials(host, null, null);
    }
}