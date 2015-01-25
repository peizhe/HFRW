package com.kol.dbPlugin;

import com.intellij.openapi.project.Project;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.jdbc.DatabaseConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum LinkDatabaseError {

    EMPTY_DATABASE_HOST(true, "Host can not be empty") {
        @Override
        public boolean isSettingsError(@NotNull Project project, @NotNull Settings settings) {
            return Util.Str.isEmpty(settings.getHost());
        }
    },
    EMPTY_DATABASE_NAME(true, "Database name can not be empty") {
        @Override
        public boolean isSettingsError(@NotNull Project project, @NotNull Settings settings) {
            return Util.Str.isEmpty(settings.getDatabase());
        }
    },
    ALREADY_LINKED_DATABASE(true, "This database already linked to your project") {
        @Override
        public boolean isSettingsError(@NotNull Project project, @NotNull Settings settings) {
//            final FSSettingsManager manager = FSSettingsManager.instance();
            return false;//manager.getAllSettings(project).stream()
//                    .filter(s -> s.getHost().equals(settings.getHost()))
//                    .filter(s -> s.getDatabase().equals(settings.getDatabase()))
//                    .count() > 0;
        }
    },
    EMPTY_USERNAME(true, "UserName can not be empty") {
        @Override
        public boolean isCredentialsError(@NotNull Project project, @NotNull Credentials credentials) {
            return Util.Str.isEmpty(credentials.getUsername());
        }
    },
    TEST_CONNECTION_FAILED(false, "Connection to database is failed (some settings is incorrect), please use \"Test Connection\" button") {
        @Override
        public boolean isConnectionError(@NotNull Credentials credentials, @NotNull Settings settings) {
            return !DatabaseConnector.isCorrectDBProperties(credentials, settings);
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

    public boolean isSettingsError(@NotNull Project project, @NotNull Settings settings) {
        return false;
    }

    public boolean isCredentialsError(@NotNull Project project, @NotNull Credentials credentials) {
        return false;
    }

    public boolean isConnectionError(@NotNull Credentials credentials, @NotNull Settings settings) {
        return false;
    }

    @Nullable
    public static LinkDatabaseError validate(@NotNull Project project, @NotNull Settings settings, @NotNull Credentials credentials) {
        for (LinkDatabaseError error : LinkDatabaseError.values()) {
            if(error.isSettingsError(project, settings) || error.isCredentialsError(project, credentials) ||
                    error.isConnectionError(credentials, settings)) {
                return error;
            }
        }
        return null;
    }
}
