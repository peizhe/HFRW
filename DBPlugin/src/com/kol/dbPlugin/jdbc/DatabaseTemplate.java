package com.kol.dbPlugin.jdbc;

import com.kol.dbPlugin.beans.ConnectionData;
import com.kol.dbPlugin.exceptions.DatabaseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTemplate implements DatabaseOperations {

    private ConnectionData data;

    public DatabaseTemplate(@NotNull ConnectionData data) {
        this.data = data;
    }

    @Override
    public <T> List<T> query(@NotNull String sql, @NotNull ProcessRow<T> mapper) throws DatabaseException {
        return list(sql, mapper, null);
    }

    @Override
    public <T> List<T> query(@NotNull String sql, @NotNull ProcessRow<T> mapper, @NotNull Object... params) throws DatabaseException {
        if(params.length == 0) {
            throw new DatabaseException("Parameters list can not be empty");
        }
        return list(sql, mapper, params);
    }

    @Override
    public <T> List<T> queryForList(@NotNull String sql, @NotNull Class<T> clazz) throws DatabaseException {
        return list(sql, rs -> rs.getObject(1, clazz), null);
    }

    @Override
    public <T> List<T> queryForList(@NotNull String sql, @NotNull Class<T> clazz, @NotNull Object... params) throws DatabaseException {
        if(params.length == 0) {
            throw new DatabaseException("Parameters list can not be empty");
        }
        return list(sql, rs -> rs.getObject(1, clazz), params);
    }

    @Override
    public <T> T queryForObject(@NotNull String sql, @NotNull Class<T> clazz) throws DatabaseException {
        return object(sql, rs -> rs.getObject(1, clazz), null);
    }

    @Override
    public <T> T queryForObject(@NotNull String sql, @NotNull Class<T> clazz, @NotNull Object... params) throws DatabaseException {
        return object(sql, rs -> rs.getObject(1, clazz), params);
    }

    @Override
    public <T> T queryForObject(@NotNull String sql, @NotNull ProcessRow<T> mapper) throws DatabaseException {
        return object(sql, mapper, null);
    }

    @Override
    public <T> T queryForObject(@NotNull String sql, @NotNull ProcessRow<T> mapper, @NotNull Object... params) throws DatabaseException {
        return object(sql, mapper, params);
    }

    @Override
    public int update(@NotNull String sql) throws DatabaseException {
        return updateDB(sql, null);
    }

    @Override
    public int update(@NotNull String sql, @NotNull Object... params) throws DatabaseException {
        return updateDB(sql, params);
    }

    @Override
    public int[] batchUpdate(@NotNull String[] sql) throws DatabaseException {
        if(sql.length == 0){
            throw new DatabaseException("List of SQL queries is empty");
        }
        final Connection connection = DatabaseConnector.connect(data);
        try {
            final PreparedStatement statement = connection.prepareStatement(sql[0]);
            connection.setAutoCommit(false);
            statement.setEscapeProcessing(true);
            for (int i = 1; i < sql.length; i++) {
                if (null == sql[i] || sql[i].isEmpty()) {
                    throw new DatabaseException(i + "th SQL query is null or empty");
                } else {
                    statement.addBatch(sql[i]);
                }
            }
            final int[] updates = statement.executeBatch();
            connection.commit();
            statement.close();
            return updates;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DatabaseException(e1);
            }
            throw new DatabaseException(e);
        } finally {
            DatabaseConnector.closeConnection(connection);
        }
    }

    @Override
    public int[] batchUpdate(@NotNull String sql, @NotNull List<Object[]> params) throws DatabaseException {
        final Connection connection = DatabaseConnector.connect(data);
        try {
            final PreparedStatement statement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            statement.setEscapeProcessing(true);
            if(params.size() > 0) {
                for (int j = 0; j < params.size(); j++) {
                    if (null != params.get(j) && params.get(j).length > 0) {
                        for (int i = 0; i < params.get(j).length; i++) {
                            if (null == params.get(j)[i]) {
                                throw new DatabaseException(i + "th parameter in " + j + "th listItem is null. Do not use null in parameter list");
                            } else {
                                statement.setObject(i + 1, params.get(j)[i]);
                            }
                        }
                    } else {
                        throw new DatabaseException(j + "th listItem is null or empty");
                    }
                    statement.addBatch();
                }
            }
            final int[] updates = statement.executeBatch();
            connection.commit();
            statement.close();
            return updates;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new DatabaseException(e1);
            }
            throw new DatabaseException(e);
        } finally {
            DatabaseConnector.closeConnection(connection);
        }
    }

    private <T> T object(@NotNull String sql, @NotNull ProcessRow<T> mapper, @Nullable Object[] params) throws DatabaseException {
        T obj;
        final Connection connection = DatabaseConnector.connect(data);
        try {
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setEscapeProcessing(true);
            if(null != params && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if(null == params[i]) {
                        throw new DatabaseException(i + "th parameter is null. Do not use null in parameter list");
                    } else {
                        statement.setObject(i+1, params[i]);
                    }
                }
            }
            final ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            obj = mapper.apply(resultSet);
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DatabaseConnector.closeConnection(connection);
        }
        return obj;
    }

    private <T> List<T> list(@NotNull String sql, @NotNull ProcessRow<T> mapper, @Nullable Object[] params) throws DatabaseException {
        final List<T> results = new ArrayList<>();
        final Connection connection = DatabaseConnector.connect(data);
        try {
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setEscapeProcessing(true);
            if(null != params && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if (null == params[i]) {
                        throw new DatabaseException(i + "th parameter is null. Do not use null in parameter list");
                    } else {
                        statement.setObject(i + 1, params[i]);
                    }
                }
            }
            final ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                results.add(mapper.apply(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DatabaseConnector.closeConnection(connection);
        }
        return results;
    }

    private int updateDB(@NotNull String sql, @Nullable Object[] params) throws DatabaseException {
        final Connection connection = DatabaseConnector.connect(data);
        try {
            final PreparedStatement statement = connection.prepareStatement(sql);
            statement.setEscapeProcessing(true);
            if(null != params && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    if(null == params[i]) {
                        throw new DatabaseException(i + "th parameter is null. Do not use null in parameter list");
                    } else {
                        statement.setObject(i+1, params[i]);
                    }
                }
            }
            final int updates = statement.executeUpdate();
            statement.close();
            return updates;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            DatabaseConnector.closeConnection(connection);
        }
    }
}