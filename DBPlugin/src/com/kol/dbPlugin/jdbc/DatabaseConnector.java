package com.kol.dbPlugin.jdbc;

import com.kol.dbPlugin.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    public static final int TIMEOUT = 10;

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

    public static boolean isCorrectDBProperties(@NotNull final ConnectionData data) {
        try {
            Class.forName(data.getDriverName());
            final Connection connection = DriverManager.getConnection(data.getUrl(), data.getUser(), data.getPassword());
            return connection.isValid(TIMEOUT);
        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean testConnect(@NotNull final ConnectionData data) throws SQLException {
        try {
            Class.forName(data.getDriverName());
        } catch (ClassNotFoundException ignored) {}
        return DriverManager.getConnection(data.getUrl(), data.getUser(), data.getPassword()).isValid(TIMEOUT);
    }
}