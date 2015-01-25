package com.kol.dbPlugin.interfaces;

import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface LinkDatabaseOkCallback {
    boolean apply(@NotNull Credentials credentials, @NotNull Settings settings);
}