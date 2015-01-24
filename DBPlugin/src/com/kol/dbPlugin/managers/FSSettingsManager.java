package com.kol.dbPlugin.managers;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;
import com.intellij.openapi.project.Project;
import com.kol.dbPlugin.Credentials;
import com.kol.dbPlugin.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

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
        return saveCredentials(credentialsBasePath, credentials, CREDENTIALS_FOLDER_NAME, CREDENTIALS_FILE_NAME);
    }

    @NotNull
    public Credentials getCredentials(@NotNull final String server) {
        return getCredentials(credentialsBasePath, server, CREDENTIALS_FOLDER_NAME, CREDENTIALS_FILE_NAME);
    }

    public boolean isExistCredentialsFile() {
        return isExistFile(credentialsBasePath, CREDENTIALS_FOLDER_NAME, CREDENTIALS_FILE_NAME);
    }
    /** ========================================= PUBLIC CREDENTIALS METHODS ======================================= **/

    /** ========================================= PRIVATE CREDENTIALS METHODS ====================================== **/
    @NotNull
    private Credentials getCredentials(@NotNull final Path basePath, @NotNull final String serverName, @NotNull final String folderName, @NotNull final String fileName) {
        final Path file;
        if(!isExistCredentialsFile()) {
            file = createEmptySettings(basePath, folderName, fileName);
        } else {
            file = getFile(basePath, folderName, fileName);
        }
        final Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(file));
            final String username = serverName + SERVER_PROPERTY_SEPARATOR + USERNAME_PROPERTY_KEY;
            final String password = serverName + SERVER_PROPERTY_SEPARATOR + PASSWORD_PROPERTY_KEY;
            final String server = serverName + SERVER_PROPERTY_SEPARATOR + SERVER_NAME_PROPERTY_KEY;
            if(!properties.containsKey(username) ||
                    !properties.containsKey(password) ||
                    !properties.containsKey(server)) {
                return new Credentials();
            }
            return new Credentials(
                    serverName,
                    properties.getProperty(username),
                    properties.getProperty(password)
            );
        } catch (IOException ignored){}
        return new Credentials();
    }

    private boolean saveCredentials(@NotNull final Path basePath, @NotNull final Credentials credentials, @NotNull final String folderName, @NotNull final String fileName) {
        if(!isExistCredentialsFile()) {
            createEmptySettings(basePath, folderName, fileName);
        }
        final Path file = getFile(basePath, folderName, fileName);
        final Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(file));
            properties.setProperty(credentials.getServer() + SERVER_PROPERTY_SEPARATOR + USERNAME_PROPERTY_KEY, credentials.getUsername());
            properties.setProperty(credentials.getServer() + SERVER_PROPERTY_SEPARATOR + PASSWORD_PROPERTY_KEY, credentials.getPassword());
            properties.setProperty(credentials.getServer() + SERVER_PROPERTY_SEPARATOR + SERVER_NAME_PROPERTY_KEY, credentials.getServer());
            properties.store(Files.newOutputStream(file), "Created/updated Credentials for " + credentials.getServer());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    /** ========================================= PRIVATE CREDENTIALS METHODS ====================================== **/

    /** ===================================== PUBLIC DATABASE SETTINGS METHODS ===================================== **/
    public boolean saveSettings(@NotNull final Settings settings) {
        return saveSettings(credentialsBasePath, settings, PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }

    @NotNull
    public Settings getSettings(@NotNull final String server) {
        return getSettings(credentialsBasePath, server, PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }

    public boolean isExistSettingsFile() {
        return isExistFile(credentialsBasePath, PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }
    /** ===================================== PUBLIC DATABASE SETTINGS METHODS ===================================== **/

    /** ========================================= PRIVATE CREDENTIALS METHODS ====================================== **/
    @NotNull
    private Settings getSettings(@NotNull final Path basePath, @NotNull final String serverName, @NotNull final String folderName, @NotNull final String fileName) {
        final Path file;
        if(!isExistSettingsFile()) {
            file = createEmptySettings(basePath, folderName, fileName);
        } else {
            file = getFile(basePath, folderName, fileName);
        }
        final Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(file));
            final String url = serverName + SERVER_PROPERTY_SEPARATOR + SERVER_URL_PROPERTY_KEY;
            final String port = serverName + SERVER_PROPERTY_SEPARATOR + SERVER_PORT_PROPERTY_KEY;
            final String server = serverName + SERVER_PROPERTY_SEPARATOR + SERVER_NAME_PROPERTY_KEY;
            final String database = serverName + SERVER_PROPERTY_SEPARATOR + SERVER_DATABASE_PROPERTY_KEY;
            if(!properties.containsKey(url) ||
                    !properties.containsKey(port) ||
                    !properties.containsKey(database) ||
                    !properties.containsKey(server)) {
                return new Settings();
            }
            return new Settings(
                    serverName,
                    properties.getProperty(url),
                    properties.getProperty(port),
                    properties.getProperty(database)
            );
        } catch (IOException ignored){}
        return new Settings();
    }

    private boolean saveSettings(@NotNull final Path basePath, @NotNull final Settings settings, @NotNull final String folderName, @NotNull final String fileName) {
        if(!isExistCredentialsFile()) {
            createEmptySettings(basePath, folderName, fileName);
        }
        final Path file = getFile(basePath, folderName, fileName);
        final Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(file));
            properties.setProperty(settings.getServer() + SERVER_PROPERTY_SEPARATOR + SERVER_URL_PROPERTY_KEY, settings.getUrl());
            properties.setProperty(settings.getServer() + SERVER_PROPERTY_SEPARATOR + SERVER_PORT_PROPERTY_KEY, settings.getPort());
            properties.setProperty(settings.getServer() + SERVER_PROPERTY_SEPARATOR + SERVER_NAME_PROPERTY_KEY, settings.getServer());
            properties.setProperty(settings.getServer() + SERVER_PROPERTY_SEPARATOR + SERVER_DATABASE_PROPERTY_KEY, settings.getDatabase());
            properties.store(Files.newOutputStream(file), "Created/updated Settings for " + settings.getServer());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    /** ========================================= PRIVATE CREDENTIALS METHODS ====================================== **/

    public Collection<String> getSettingsServerNames(final Project project) {
        return getServerNames(Paths.get(project.getBasePath()), PLUGIN_FOLDER_NAME, PLUGIN_SETTINGS_FILE_NAME);
    }

    public Collection<String> getCredentialsServerNames(final Project project) {
        return getServerNames(Paths.get(project.getBasePath()), CREDENTIALS_FOLDER_NAME, CREDENTIALS_FILE_NAME);
    }

    private static Collection<String> getServerNames(final Path basePath, final String folderName, final String fileName) {
        final Path file = getFile(basePath, folderName, fileName);
        final Properties properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(file));
            return getServerNames(properties);
        } catch (IOException ignored){}
        return new ArrayList<>();
    }

    @NotNull
    private static ImmutableSet<String> getServerNames(@NotNull final Properties properties) {
        return Multimaps.index(properties.stringPropertyNames(), name -> name.replaceAll("\\.*$", "")).keySet();
    }

    @NotNull
    private static Path createEmptySettings(@NotNull final Path basePath, @NotNull final String folderName, @NotNull final String fileName) {
        final Path folder = getFolder(basePath, folderName);
        final Path file = getFile(basePath, folderName, fileName);
        try {
            if(!Files.exists(folder)) {
                Files.createDirectory(folder);
                makeHidden(folder);
                Files.createFile(file);
            } else if(!Files.exists(file)){
                Files.createFile(file);
            }
        } catch (IOException ignored) {}
        return file;
    }

    private static boolean isExistFile(@NotNull final Path basePath, @NotNull final String folderName, @NotNull final String fileName) {
        return Files.exists(getFolder(basePath, folderName)) && Files.exists(getFile(basePath, folderName, fileName));
    }

    @NotNull
    private static Path getFolder(@NotNull final Path basePath, @NotNull final String folderName) {
        return basePath.resolve(folderName);
    }

    @NotNull
    private static Path getFile(@NotNull final Path basePath, @NotNull final String folderName, @NotNull final String fileName) {
        return getFolder(basePath, folderName).resolve(fileName);
    }

    private static void makeHidden(@NotNull final Path path) {
        try {
            Files.setAttribute(path, "dos:hidden", true);
        } catch (IOException ignored) {}
    }
}