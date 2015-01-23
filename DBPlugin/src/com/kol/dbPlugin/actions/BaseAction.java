package com.kol.dbPlugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.kol.dbPlugin.Credentials;
import com.kol.dbPlugin.exceptions.DBException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class BaseAction extends AnAction {

    protected static final String PLUGIN_FOLDER_NAME = "database1";
    protected static final String PLUGIN_SETTINGS_NAME = "DBTool.cfg";
    protected static final String PROPERTIES_FOLDER_NAME = ".DBToolPlugin";
    protected static final String PROPERTIES_FILE_NAME_SUFFIX = ".properties";
    protected static final Path BASE_PATH = Paths.get(System.getProperty("user.home"));
    public static final String USERNAME_PROPERTY_KEY = "username";
    public static final String PASSWORD_PROPERTY_KEY = "password";

    protected static Path createPluginFolder(final Path basePath) {
        final Path pluginFolder = basePath.resolve(PLUGIN_FOLDER_NAME);
        if(!Files.exists(pluginFolder)) {
            try {
                Files.createDirectory(pluginFolder);
            } catch (IOException ignored) {}
        }
        return pluginFolder;
    }

    protected static Path createDatabaseFolder(final Path pluginFolder, final String databaseName) {
        final Path dbFolder = pluginFolder.resolve(databaseName);
        if(!Files.exists(dbFolder)) {
            try {
                Files.createDirectory(dbFolder);
            } catch (IOException ignored) {}
        }
        return dbFolder;
    }

    protected static Path createPluginSettings(final Path pluginFolder) {
        final Path settings = pluginFolder.resolve(PLUGIN_SETTINGS_NAME);
        if(!Files.exists(settings)) {
            try {
                Files.createFile(settings);
            } catch (IOException ignored) {}
        }
        return settings;
    }

    protected static void safeSaveCredentials(final Credentials credentials) {
        try {
            saveCredentials(BASE_PATH, credentials);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    protected static Credentials safeGetCredentials(final String server) {
        try {
            return getCredentials(BASE_PATH, server);
        } catch (DBException e) {
            e.printStackTrace();
            return new Credentials("", "");
        }
    }

    protected static void saveCredentials(final Path basePath, final Credentials credentials) throws DBException {
        final Path folder = getPropertiesFolder(basePath);
        final Path file;
        if(!isExistPropertyFile(folder, credentials.getServer())) {
            file = createProperties(basePath, credentials.getServer());
        } else {
            file = getPropertiesFile(folder, credentials.getServer());
        }
        try {
            final Properties properties = new Properties();
            properties.setProperty(USERNAME_PROPERTY_KEY, credentials.getUsername());
            properties.setProperty(PASSWORD_PROPERTY_KEY, credentials.getPassword());
            properties.store(Files.newOutputStream(file), "Database Credentials for " + credentials.getServer());
        } catch (IOException e) {
            throw new DBException(e);
        }
    }

    protected static Credentials getCredentials(final Path basePath, final String server) throws DBException {
        final Path folder = getPropertiesFolder(basePath);
        if(isExistPropertyFile(folder, server)) {
            final Properties properties = new Properties();
            try {
                properties.load(Files.newBufferedReader(getPropertiesFile(folder, server)));
            } catch (IOException e) {
                throw new DBException(e);
            }
            return new Credentials(server, properties.getProperty(USERNAME_PROPERTY_KEY), properties.getProperty(PASSWORD_PROPERTY_KEY));
        } else {
            throw new DBException("Credentials are not exist");
        }
    }

    private static Path createProperties(final Path basePath, final String server) {
        return createPropertiesFile(createPropertiesFolder(basePath), server);
    }

    private static Path createPropertiesFile(final Path folder, final String server) {
        if(!isExistPropertyFile(folder, server)) {
            try {
                return Files.createFile(getPropertiesFile(folder, server));
            } catch (IOException e) {
                return null;
            }
        } else {
            return getPropertiesFile(folder, server);
        }
    }

    private static Path createPropertiesFolder(final Path basePath) {
        if(!isExistPropertyFolder(basePath)) {
            try {
                final Path directory = Files.createDirectory(getPropertiesFolder(basePath));
                return null == directory ? null : Files.setAttribute(directory, "dos:hidden", true);
            } catch (IOException e) {
                return null;
            }
        } else {
            return getPropertiesFolder(basePath);
        }
    }

    private static boolean isExistPropertyFolder(final Path basePath) {
        return Files.exists(getPropertiesFolder(basePath));
    }

    private static Path getPropertiesFolder(final Path basePath) {
        return basePath.resolve(PROPERTIES_FOLDER_NAME);
    }

    private static boolean isExistPropertyFile(final Path folder, final String server) {
        return Files.exists(folder) && Files.exists(getPropertiesFile(folder, server));
    }

    private static Path getPropertiesFile(final Path folder, final String server) {
        return folder.resolve(server + PROPERTIES_FILE_NAME_SUFFIX);
    }
}