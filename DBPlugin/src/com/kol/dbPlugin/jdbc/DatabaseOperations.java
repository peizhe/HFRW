package com.kol.dbPlugin.jdbc;

import com.kol.dbPlugin.exceptions.DatabaseException;

import java.util.List;

public interface DatabaseOperations {

    <T> List<T> query(String sql, ProcessRow<T> mapper) throws DatabaseException;

    <T> List<T> query(String sql, ProcessRow<T> mapper, Object ... params) throws DatabaseException;

    <T> List<T> queryForList(String sql, Class<T> clazz) throws DatabaseException;

    <T> List<T> queryForList(String sql, Class<T> clazz, Object ... params) throws DatabaseException;

    <T> T queryForObject(String sql, Class<T> clazz) throws DatabaseException;

    <T> T queryForObject(String sql, Class<T> clazz, Object ... params) throws DatabaseException;

    <T> T queryForObject(String sql, ProcessRow<T> mapper) throws DatabaseException;

    <T> T queryForObject(String sql, ProcessRow<T> mapper, Object ... params) throws DatabaseException;

    int update(String sql) throws DatabaseException;

    int update(String sql, Object ... params) throws DatabaseException;

    int[] batchUpdate(String[] sql) throws DatabaseException;

    int[] batchUpdate(String sql, List<Object[]> params) throws DatabaseException;
}