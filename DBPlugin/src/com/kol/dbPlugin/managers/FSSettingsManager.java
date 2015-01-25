package com.kol.dbPlugin.managers;

import com.intellij.openapi.project.Project;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.Util;
import com.kol.dbPlugin.jdbc.ConnectionData;
import com.kol.dbPlugin.jdbc.DatabaseConnector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collection;

public class FSSettingsManager implements Constants {

    private Path credentialsBasePath;
    private static FSSettingsManager manager;

    private FSSettingsManager() {
        manager = this;
    }

    private FSSettingsManager(Path basePath) {
        this();
        this.credentialsBasePath = basePath;
    }

    public static FSSettingsManager instance() {
        return instance(Paths.get(System.getProperty("user.home")));
    }

    public static FSSettingsManager instance(@NotNull final Path basePath) {
        if(null == manager) {
            manager = new FSSettingsManager(basePath);
        }
        return manager;
    }

    /** ========================================= PUBLIC CREDENTIALS METHODS ======================================= **/
    public boolean saveCredentials(@NotNull final Credentials credentials) {
        return Util.PluginFileSystem.saveCredentials(credentialsBasePath, credentials, CREDENTIALS_FOLDER_NAME, CREDENTIALS_FILE_NAME);
    }

    @NotNull
    public Credentials getCredentials(@NotNull final String host) {
        return Util.PluginFileSystem.getCredentials(credentialsBasePath, host, CREDENTIALS_FOLDER_NAME, CREDENTIALS_FILE_NAME);
    }

    public boolean isExistCredentialsFile() {
        return Util.PluginFileSystem.isExistFile(credentialsBasePath, CREDENTIALS_FOLDER_NAME, CREDENTIALS_FILE_NAME);
    }
    /** ========================================= PUBLIC CREDENTIALS METHODS ======================================= **/

    /** ===================================== PUBLIC DATABASE SETTINGS METHODS ===================================== **/
    public boolean saveSettings(@NotNull final Project project, @NotNull final Settings settings) {
        return Util.PluginFileSystem.saveSettings(Paths.get(project.getBasePath()), settings, PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }

    @NotNull
    public Settings getSettings(@NotNull final Project project, @NotNull final String host, @NotNull final String db) {
        return Util.PluginFileSystem.getSettings(Paths.get(project.getBasePath()), host, db, PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }

    public boolean isExistSettingsFile(@NotNull final Project project) {
        return Util.PluginFileSystem.isExistFile(Paths.get(project.getBasePath()), PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }
    /** ===================================== PUBLIC DATABASE SETTINGS METHODS ===================================== **/

    public Collection<Settings> getAllSettings(final Project project) {
        return Util.PluginFileSystem.getAllSettings(Paths.get(project.getBasePath()), PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }

    public boolean isCorrect(@NotNull final Credentials credentials, @NotNull final Settings settings) {
        final ConnectionData data = new ConnectionData(
                Util.Database.makeDBUrl(settings.getHost(), settings.getPort(), settings.getDatabase()),
                credentials.getUsername(),
                credentials.getPassword(),
                MySQL_DATABASE_DRIVER_NAME
        );
        return DatabaseConnector.isCorrectDBProperties(data);
    }

    @Nullable
    public String testConnect(@NotNull final Credentials credentials, @NotNull final Settings settings) {
        final ConnectionData data = new ConnectionData(
                Util.Database.makeDBUrl(settings.getHost(), settings.getPort(), settings.getDatabase()),
                credentials.getUsername(),
                credentials.getPassword(),
                MySQL_DATABASE_DRIVER_NAME
        );
        try {
            DatabaseConnector.testConnect(data);
        } catch (SQLException e) {
            return e.getMessage();
        }
        return null;
    }

    public void createDatabaseFolder(@NotNull final Project project, @NotNull final Settings settings) {
        final String name = Util.Database.makeDBFolderName(settings.getHost(), settings.getDatabase());
        try {
            Files.createDirectory(Util.PluginFileSystem.getFolder(Paths.get(project.getBasePath()), PLUGIN_FOLDER_NAME).resolve(name));
        } catch (IOException ignored) {}
    }
}