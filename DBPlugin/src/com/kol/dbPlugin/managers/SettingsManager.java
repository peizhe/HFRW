package com.kol.dbPlugin.managers;

import com.kol.dbPlugin.C;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.exceptions.FileSystemException;
import com.kol.dbPlugin.interfaces.PropertyManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SettingsManager extends PropertyManager<Settings> {

    public SettingsManager(Path basePath, String dirName) {
        super(basePath.resolve(dirName));
    }

    @NotNull
    @Override
    public Settings get(@NotNull final Settings placeholder) {
        final String host = placeholder.getHost();
        final String dbName = placeholder.getDatabase();
        final Path file = directory.resolve(host).resolve(dbName).resolve(C.SETTINGS_FILE_NAME);
        if(!Files.exists(file)) {
            return new Settings();
        } else {
            final Properties prop = new Properties();
            try {
                prop.load(Files.newBufferedReader(file));
                return new Settings(placeholder.getHost(), prop.getProperty(C.SETTINGS_PORT_KEY), placeholder.getDatabase());
            } catch (IOException e) {
                return new Settings();
            }
        }
    }

    @Override
    public void save(@NotNull final Settings data) {
        final Path folder = directory.resolve(data.getHost()).resolve(data.getDatabase());
        final Path file = folder.resolve(C.SETTINGS_FILE_NAME);
        if(!Files.exists(file)) {
            try {
                if(!Files.exists(folder)) {
                    Files.createDirectories(folder);
                }
                Files.createFile(file);
            } catch (IOException e) {
                throw new FileSystemException(e);
            }
        }
        final Properties prop = new Properties();
        try {
            prop.setProperty(C.SETTINGS_HOST_KEY, data.getHost());
            prop.setProperty(C.SETTINGS_PORT_KEY, data.getPort());
            prop.setProperty(C.SETTINGS_DATABASE_KEY, data.getDatabase());
            prop.store(Files.newBufferedWriter(file), "Database Settings");
        } catch (IOException e) {
            throw new FileSystemException(e);
        }
    }
}