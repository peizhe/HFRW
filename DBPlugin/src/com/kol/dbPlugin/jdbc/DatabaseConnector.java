package com.kol.dbPlugin.jdbc;

import com.kol.dbPlugin.C;
import com.kol.dbPlugin.Util;
import com.kol.dbPlugin.beans.ConnectionData;
import com.kol.dbPlugin.beans.Credentials;
import com.kol.dbPlugin.beans.Settings;
import com.kol.dbPlugin.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

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

    public static boolean isCorrectDBProperties(@NotNull final Settings settings, @NotNull final Credentials credentials) {
        return Util.Str.isEmpty(testConnect(settings, credentials));
    }

    @Nullable
    public static String testConnect(@NotNull final Settings settings, @NotNull final Credentials credentials) {
        final ConnectionData data = new ConnectionData(
                Util.Database.makeDBUrl(settings.getHost(), settings.getPort(), settings.getDatabase()),
                credentials.getUsername(),
                credentials.getPassword(),
                C.MySQL_DATABASE_DRIVER_NAME
        );
        try {
            Class.forName(data.getDriverName());
            DriverManager.getConnection(data.getUrl(), data.getUser(), data.getPassword()).isValid(TIMEOUT);
        } catch (SQLException | ClassNotFoundException e) {
            return e.getMessage();
        }
        return null;
    }
}