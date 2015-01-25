package com.kol.dbPlugin.managers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kol.dbPlugin.C;
import com.kol.dbPlugin.Util;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.exceptions.FileSystemException;
import com.kol.dbPlugin.interfaces.PropertyManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CredentialsManager extends PropertyManager<Credentials> {

    private Path file;

    public CredentialsManager(Path basePath, String dirName) {
        super(basePath.resolve(dirName));
        file = directory.resolve(C.CREDENTIALS_FILE_NAME);
        createFileIfNotExist(file);
    }

    @NotNull
    @Override
    public Credentials get(@NotNull final Credentials placeholder) {
        final String host = placeholder.getHost();
        final Optional<Credentials> credentials = getAll().stream().filter(c -> host.equals(c.getHost())).findFirst();
        if(credentials.isPresent()) {
            return credentials.get();
        } else {
            return new Credentials();
        }
    }

    private List<Credentials> getAll() {
        try {
            final String collect = Files.readAllLines(file).stream().collect(Collectors.joining());
            if(Util.Str.isEmpty(collect)) {
                return new ArrayList<>();
            } else {
                final Credentials[] json = new Gson().fromJson(collect, Credentials[].class);
                final List<Credentials> list = new ArrayList<>();
                Collections.addAll(list, json);
                return list;
            }
        } catch (IOException e) {
            throw new FileSystemException(e);
        }
    }

    @Override
    public void save(@NotNull final Credentials data) {
        final List<Credentials> list = getAll();
        final Optional<Credentials> first = list.stream().filter(c -> c.getHost().equals(data.getHost())).findFirst();
        if(first.isPresent()) {
            first.get().update(data);
        } else {
            list.add(data);
        }
        saveAll(list);
    }

    private void saveAll(@NotNull final List<Credentials> data) {
        final List<JsonObject> collect = data.stream().map(Credentials::toGson).collect(Collectors.toList());
        final JsonArray array = new JsonArray();
        collect.forEach(array::add);
        try {
            Files.write(file, array.toString().getBytes());
        } catch (IOException e) {
            throw new FileSystemException(e);
        }
    }

    private static void createFileIfNotExist(@NotNull final Path file) {
        if(!Files.exists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                throw new FileSystemException(e);
            }
        }
    }
}