package com.kol.dbPlugin.interfaces;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

public interface Property<T> {

    @NotNull
    default Gson toGson() {
        final Gson gson = new Gson();
        gson.toJsonTree(this);
        return gson;
    }

    boolean exist();

    String getHost();

    void update(@NotNull T data);
}