package com.kol.dbPlugin.interfaces;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class PropertyManager<T extends Property> {

    protected Path directory;

    protected PropertyManager(Path directory) {
        this.directory = directory;
        createDirectoriesIfNotExist(directory);
    }

    @NotNull
    public abstract T get(@NotNull T placeholder);

    public abstract void save(@NotNull T data);

    protected boolean exist(@NotNull final String fileName) {
        final Path resolve = directory.resolve(fileName);
        return Files.exists(resolve) && !Files.isDirectory(resolve);
    }

    private void createDirectoriesIfNotExist(@NotNull final Path path) {
        if(!Files.exists(path) || !Files.isDirectory(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ignored) {}
        }
    }
}