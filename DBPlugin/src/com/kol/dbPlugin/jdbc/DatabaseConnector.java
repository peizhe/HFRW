package com.kol.dbPlugin.jdbc;

import com.kol.dbPlugin.Util;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String MySQL_DATABASE_DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final int TIMEOUT = 10;

    private DatabaseConnector(){}

    public static Connection connect(@NotNull final ConnectionData data) {
        try {
            Class.forName(data.getDriverName());
            return DriverManager.getConnection(data.getUrl(), data.getUser(), data.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static void closeConnection(@Nullable final Connection connection) {
        try {
            if(null != connection && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ignored) {}
    }

    public static boolean isCorrectDBProperties(@NotNull final Credentials credentials, @NotNull final Settings settings) {
        final ConnectionData data = new ConnectionData(
                Util.Database.makeDBUrl(settings.getHost(), settings.getPort(), settings.getDatabase()),
                credentials.getUsername(),
                credentials.getPassword(),
                MySQL_DATABASE_DRIVER_NAME
        );
        try {
            Class.forName(data.getDriverName());
            final Connection connection = DriverManager.getConnection(data.getUrl(), data.getUser(), data.getPassword());
            return connection.isValid(TIMEOUT);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }

    @Nullable
    public static String testConnect(@NotNull final Settings settings, @NotNull final Credentials credentials) {
        final ConnectionData data = new ConnectionData(
                Util.Database.makeDBUrl(settings.getHost(), settings.getPort(), settings.getDatabase()),
                credentials.getUsername(),
                credentials.getPassword(),
                MySQL_DATABASE_DRIVER_NAME
        );
        try {
            Class.forName(data.getDriverName());
        } catch (ClassNotFoundException ignored) {}

        try {
            DriverManager.getConnection(data.getUrl(), data.getUser(), data.getPassword()).isValid(TIMEOUT);
        } catch (SQLException e) {
            return e.getMessage();
        }
        return null;
    }
}