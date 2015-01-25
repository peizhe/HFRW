package com.kol.dbPlugin;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.managers.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

public class Util implements Constants {

    public static final String SERVER_PROPERTY_SEPARATOR = ":";
    public static final String USERNAME_PROPERTY_KEY = "username";
    public static final String PASSWORD_PROPERTY_KEY = "password";

    public static final String SERVER_HOST_PROPERTY_KEY = "host";
    public static final String SERVER_PORT_PROPERTY_KEY = "port";
    public static final String SERVER_DATABASE_PROPERTY_KEY = "database";

    private Util() {}
    
    public static final class Str {
        private Str() {}
        
        public static boolean isEmpty(@Nullable final String str) {
            return null == str || str.trim().isEmpty();
        }

        public static boolean isNonEmpty(@Nullable final String str) {
            return !isEmpty(str);
        }
    }
    
    public static final class PluginFileSystem {
        private PluginFileSystem() {}

        @NotNull
        public static Collection<Settings> getAllSettings(@NotNull final Path basePath, @NotNull final String folderName, @NotNull final String fileName) {
            final Path file = getFile(basePath, folderName, fileName);
            final Properties properties = new Properties();
            try {
                properties.load(Files.newBufferedReader(file));
                final ImmutableSet<String> strings = Multimaps.index(properties.stringPropertyNames(), name -> {
                    final String[] split = name.split(SERVER_PROPERTY_SEPARATOR);
                    return split[0] + SERVER_PROPERTY_SEPARATOR + split[1];
                }).keySet();
                return strings.stream().map(str -> getSettings(str, properties)).collect(Collectors.toList());
            } catch (IOException ignored){}
            return new ArrayList<>();
        }

        @NotNull
        private static Settings getSettings(@NotNull final String key, @NotNull final Properties properties) {
            final String prefixKey = key + SERVER_PROPERTY_SEPARATOR;
            final String host = prefixKey + SERVER_HOST_PROPERTY_KEY;
            final String port = prefixKey + SERVER_PORT_PROPERTY_KEY;
            final String database = prefixKey + SERVER_DATABASE_PROPERTY_KEY;
            if(!properties.containsKey(host) || !properties.containsKey(port) || !properties.containsKey(database)) {
                return new Settings();
            }
            return new Settings(properties.getProperty(host), properties.getProperty(port), properties.getProperty(database));
        }

        @NotNull
        public static Path createEmptySettings(@NotNull final Path basePath, @NotNull final String folderName, @NotNull final String fileName) {
            final Path folder = getFolder(basePath, folderName);
            final Path file = getFile(basePath, folderName, fileName);
            try {
                if(!Files.exists(folder)) {
                    Files.createDirectory(folder);
                    Files.createFile(file);
                } else if(!Files.exists(file)){
                    Files.createFile(file);
                }
            } catch (IOException ignored) {}
            return file;
        }

        public static boolean isExistFile(@NotNull final Path basePath, @NotNull final String folderName, @NotNull final String fileName) {
            return Files.exists(getFolder(basePath, folderName)) && Files.exists(getFile(basePath, folderName, fileName));
        }

        @NotNull
        public static Path getFolder(@NotNull final Path basePath, @NotNull final String folderName) {
            return basePath.resolve(folderName);
        }

        @NotNull
        public static Path getFile(@NotNull final Path basePath, @NotNull final String folderName, @NotNull final String fileName) {
            return getFolder(basePath, folderName).resolve(fileName);
        }

        public static void makeHidden(@NotNull final Path path) {
            try {
                Files.setAttribute(path, "dos:hidden", true);
            } catch (IOException ignored) {}
        }

        @NotNull
        public static Credentials getCredentials(@NotNull final Path basePath, @NotNull final String host, @NotNull final String folderName, @NotNull final String fileName) {
            final Path file;
            if(!isExistFile(basePath, folderName, fileName)) {
                file = createEmptySettings(basePath, folderName, fileName);
                makeHidden(file);
            } else {
                file = getFile(basePath, folderName, fileName);
            }
            final Properties properties = new Properties();
            try {
                properties.load(Files.newBufferedReader(file));
                final String username = host + SERVER_PROPERTY_SEPARATOR + USERNAME_PROPERTY_KEY;
                final String password = host + SERVER_PROPERTY_SEPARATOR + PASSWORD_PROPERTY_KEY;
                if(!properties.containsKey(username) || !properties.containsKey(password) ) {
                    return new Credentials();
                }
                return new Credentials(
                        host,
                        properties.getProperty(username),
                        properties.getProperty(password)
                );
            } catch (IOException ignored){}
            return new Credentials();
        }

        public static boolean saveCredentials(@NotNull final Path basePath, @NotNull final Credentials credentials, @NotNull final String folderName, @NotNull final String fileName) {
            if(!isExistFile(basePath, folderName, fileName)) {
                final Path file = createEmptySettings(basePath, folderName, fileName);
                makeHidden(file);
            }
            final Path file = getFile(basePath, folderName, fileName);
            final Properties properties = new Properties();
            try {
                properties.load(Files.newBufferedReader(file));
                properties.setProperty(credentials.getHost() + SERVER_PROPERTY_SEPARATOR + USERNAME_PROPERTY_KEY, credentials.getUsername());
                properties.setProperty(credentials.getHost() + SERVER_PROPERTY_SEPARATOR + PASSWORD_PROPERTY_KEY, credentials.getPassword());
                properties.store(Files.newOutputStream(file), "Created/updated Credentials for " + credentials.getHost());
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @NotNull
        public static Settings getSettings(@NotNull final Path basePath, @NotNull final String host, @NotNull final String db, @NotNull final String folderName, @NotNull final String fileName) {
            final Path file;
            if(!isExistFile(basePath, folderName, fileName)) {
                file = createEmptySettings(basePath, folderName, fileName);
            } else {
                file = getFile(basePath, folderName, fileName);
            }
            final Properties properties = new Properties();
            try {
                properties.load(Files.newBufferedReader(file));
                return getSettings(host + SERVER_PROPERTY_SEPARATOR + db, properties);
            } catch (IOException ignored){}
            return new Settings();
        }

        public static boolean saveSettings(@NotNull final Path basePath, @NotNull final Settings settings, @NotNull final String folderName, @NotNull final String fileName) {
            if(!isExistFile(basePath, folderName, fileName)) {
                createEmptySettings(basePath, folderName, fileName);
            }
            final Path file = getFile(basePath, folderName, fileName);
            final Properties properties = new Properties();
            try {
                properties.load(Files.newBufferedReader(file));
                final String prefixKey = settings.getHost() + SERVER_PROPERTY_SEPARATOR + settings.getDatabase() + SERVER_PROPERTY_SEPARATOR;
                properties.setProperty(prefixKey + SERVER_HOST_PROPERTY_KEY, settings.getHost());
                properties.setProperty(prefixKey + SERVER_PORT_PROPERTY_KEY, settings.getPort());
                properties.setProperty(prefixKey + SERVER_DATABASE_PROPERTY_KEY, settings.getDatabase());
                properties.store(Files.newOutputStream(file), "Created/updated Settings for " + settings.getHost() + " (db: " + settings.getDatabase() + ")");
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    public static final class Database {
        private Database() {}

        public static String makeDBUrl(@Nullable final String host, @Nullable final String port, @Nullable final String database) {
            final StringBuilder sb = new StringBuilder(DB_URL_PREFIX);
            if(null != host) {
                sb.append(host);
            }
            if(Str.isNonEmpty(port)) {
                sb.append(DB_URL_PORT_PREFIX).append(port);
            }
            if(Str.isNonEmpty(database)) {
                sb.append(DB_URL_DATABASE_SEPARATOR).append(database);
            }
            return sb.toString();
        }

        public static String makeDBFolderName(@Nullable final String host, @Nullable final String database) {
            final StringBuilder sb = new StringBuilder();
            if(Str.isNonEmpty(database)) {
                sb.append(database);
            }
            if(null != host) {
                sb.append(DB_NAME_HOST_SEPARATOR).append(host);
            }
            return sb.toString();
        }
    }
}