package com.kol.dbPlugin.interfaces;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public interface Property<T> {

    @NotNull
    default JsonObject toGson() {
        return new JsonObject();
    }

    boolean exist();

    String getHost();

    void update(@NotNull T data);
}