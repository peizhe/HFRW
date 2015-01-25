package com.kol.dbPlugin.managers;

public interface Constants {

    public static final String PLUGIN_FOLDER_NAME = "database";
    public static final String PLUGIN_SETTINGS_FILE_NAME = "DBToolPlugin.properties";

    public static final String CREDENTIALS_FOLDER_NAME = ".DBToolPlugin";
    public static final String CREDENTIALS_FILE_NAME = "credentials.properties";

    public static final String DB_DEFAULT_PORT = "3306";
    public static final String DB_URL_PORT_PREFIX = ":";
    public static final String DB_DEFAULT_HOST = "localhost";
    public static final String DB_URL_PREFIX = "jdbc:mysql://";
    public static final String DB_URL_DATABASE_SEPARATOR = "/";

    public static final String DB_NAME_HOST_SEPARATOR = "@";

    public static final String MySQL_DATABASE_DRIVER_NAME = "com.mysql.jdbc.Driver";
}