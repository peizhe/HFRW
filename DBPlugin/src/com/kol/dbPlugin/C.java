package com.kol.dbPlugin;

import java.nio.file.Path;
import java.nio.file.Paths;

public class C {

    public static final String DB_URL_PORT_PREFIX = ":";
    public static final String DB_URL_PREFIX = "jdbc:mysql://";
    public static final String DB_URL_DATABASE_SEPARATOR = "/";

    public static final String PLUGIN_DIRECTORY_NAME = "database";

    public static final String DB_DEFAULT_PORT = "3306";
    public static final String DB_DEFAULT_HOST = "localhost";

    public static final String CREDENTIALS_DIRECTORY_NAME = ".DBToolPlugin";
    public static final Path CREDENTIALS_BASE_PATH = Paths.get(System.getProperty("user.home"));

    public static final String MySQL_DATABASE_DRIVER_NAME = "com.mysql.jdbc.Driver";

    public static final String CREDENTIALS_FILE_NAME = "credentials.json";

    public static final String SETTINGS_HOST_KEY = "host";
    public static final String SETTINGS_PORT_KEY = "port";
    public static final String SETTINGS_DATABASE_KEY = "database";
    public static final String SETTINGS_FILE_NAME = "settings.cfg";
}
