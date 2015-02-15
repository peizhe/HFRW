package com.kol.dbPlugin;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.kol.dbPlugin.beans.ConnectionData;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.DBConnectInfo;
import com.kol.dbPlugin.beans.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Util {
    public static final String EMPTY_STRING = "";
    public static final int IN_FOLDER_COUNT = 2;

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

    public static final class Database {
        private Database() {}

        public static String makeDBUrl(@Nullable final String host, @Nullable final String port, @Nullable final String database) {
            final StringBuilder sb = new StringBuilder(C.DB_URL_PREFIX);
            if(null != host) {
                sb.append(host);
            }
            if(Str.isNonEmpty(port)) {
                sb.append(C.DB_URL_PORT_PREFIX).append(port);
            }
            if(Str.isNonEmpty(database)) {
                sb.append(C.DB_URL_DATABASE_SEPARATOR).append(database);
            }
            return sb.toString();
        }

        public static ConnectionData makeConnectionData(@NotNull final Credentials credentials, @NotNull final Settings settings) {
            return new ConnectionData(
                    makeDBUrl(settings.getHost(), settings.getPort(), settings.getDatabase()),
                    credentials.getUsername(),
                    credentials.getPassword(),
                    C.MySQL_DATABASE_DRIVER_NAME
            );
        }
    }

    public static final class FS {

        private FS(){}

        public static DBConnectInfo getConnectInfo(@NotNull final AnActionEvent event) {
            final Project project = event.getProject();
            final VirtualFile actionFile = event.getData(LangDataKeys.VIRTUAL_FILE);
            if(null == actionFile || null == project || null == actionFile.getCanonicalPath()) {
                return null;
            }
            final String canonicalPath = Paths.get(actionFile.getCanonicalPath()).toString();
            final String basePath = Paths.get(project.getBasePath()).toString();
            final String restPath = canonicalPath.replace(basePath, EMPTY_STRING).replaceAll("^\\\\", EMPTY_STRING);
            final String dataFolders;
            if(restPath.startsWith(C.PLUGIN_DIRECTORY_NAME)) {
                dataFolders = restPath.replaceAll("^" + C.PLUGIN_DIRECTORY_NAME, EMPTY_STRING);
            } else {
                return null;
            }

            final List<String> split = Lists.newArrayList(Splitter.on("\\").omitEmptyStrings().split(dataFolders));
            if(split.size() < IN_FOLDER_COUNT) {
                return null;
            } else {
                return new DBConnectInfo(split.get(0), split.get(1), Paths.get(project.getBasePath()));
            }
        }

        public static void createDirectory(final Path folder) throws IOException {
            if(!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
        }

        public static void createFile(final Path file) throws IOException {
            if(!Files.exists(file)) {
                Files.createFile(file);
            }
        }
    }
}