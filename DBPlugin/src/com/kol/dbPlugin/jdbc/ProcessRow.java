package com.kol.dbPlugin.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ProcessRow<T> {
    T apply(ResultSet rs) throws SQLException;
}
