package com.kol.dbPlugin.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.exceptions.FileSystemException;
import com.kol.dbPlugin.interfaces.PropertyManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class CredentialsManager extends PropertyManager<Credentials> {

    private static final String CREDENTIALS_FILE_NAME = "credentials.json";

    private Path file;

    public CredentialsManager(Path basePath, String dirName) {
        super(basePath.resolve(dirName));
        file = directory.resolve(CREDENTIALS_FILE_NAME);
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
            return Arrays.asList(new Gson().fromJson(Files.readAllLines(file).stream().collect(Collectors.joining()), Credentials[].class));
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
        final Credentials[] credentials = data.toArray(new Credentials[data.size()]);
        final String json = new GsonBuilder().setPrettyPrinting().create().toJson(credentials, Credentials[].class);
        try {
            Files.write(file, json.getBytes());
        } catch (IOException e) {
            throw new FileSystemException(e);
        }
    }
}