package com.kol.dbPlugin;

import com.intellij.openapi.project.Project;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.jdbc.DatabaseConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Paths;

public enum LinkDatabaseError {

    EMPTY_DATABASE_HOST(true, "Host can not be empty") {
        @Override
        public boolean isError(@NotNull Project project, @NotNull Credentials credentials, @NotNull Settings settings) {
            return Util.Str.isEmpty(settings.getHost());
        }
    },
    EMPTY_DATABASE_NAME(true, "Database name can not be empty") {
        @Override
        public boolean isError(@NotNull Project project, @NotNull Credentials credentials, @NotNull Settings settings) {
            return Util.Str.isEmpty(settings.getDatabase());
        }
    },
    ALREADY_LINKED_DATABASE(true, "This database already linked to your project") {
        @Override
        public boolean isError(@NotNull Project project, @NotNull Credentials credentials, @NotNull Settings settings) {
            return Files.exists(Paths.get(project.getBasePath()).resolve(C.PLUGIN_DIRECTORY_NAME).resolve(settings.getHost()).resolve(settings.getDatabase()));
        }
    },
    EMPTY_USERNAME(true, "UserName can not be empty") {
        @Override
        public boolean isError(@NotNull Project project, @NotNull Credentials credentials, @NotNull Settings settings) {
            return Util.Str.isEmpty(credentials.getUsername());
        }
    },
    TEST_CONNECTION_FAILED(false, "Connection to database is failed (some settings is incorrect), please use \"Test Connection\" button") {
        @Override
        public boolean isError(@NotNull Project project, @NotNull Credentials credentials, @NotNull Settings settings) {
            return !DatabaseConnector.isCorrectDBProperties(settings, credentials);
        }
    };

    LinkDatabaseError(boolean blocked, String message) {
        this.blocked = blocked;
        this.message = message;
    }

    private String message;
    private boolean blocked;

    public boolean isBlocked() {
        return blocked;
    }

    public String getMessage() {
        return message;
    }

    public abstract boolean isError(@NotNull Project project, @NotNull Credentials credentials, @NotNull Settings settings);

    @Nullable
    public static LinkDatabaseError validate(@NotNull Project project, @NotNull Settings settings, @NotNull Credentials credentials) {
        for (LinkDatabaseError error : LinkDatabaseError.values()) {
            if(error.isError(project, credentials, settings)) {
                return error;
            }
        }
        return null;
    }
}
