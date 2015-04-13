package com.kol.recognition.jdbc;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.UsesJava7;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class AnnotationRowMapper<T> implements RowMapper<T> {

    private Class<T> mappedClass;
    private Map<Field, String> columns;

    public AnnotationRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        initialize();
    }

    /**
     * Initialize the mapping metadata for the given class.
     */
    private void initialize() {
        this.columns = new HashMap<>();
        ReflectionUtils.doWithFields(mappedClass, field -> {
            final Column annotation = field.getAnnotation(Column.class);
            if(null != annotation) {
                columns.put(field, annotation.name());
            }
        });
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        final T obj = instantiate(mappedClass);
        for (Field field : columns.keySet()) {
            field.setAccessible(true);
            setValue(field, obj, getResultSetValue(rs, columns.get(field), field.getType()));
        }
        return obj;
    }

    /**
     * Convenience method to instantiate a class using its no-arg constructor.
     * As this method doesn't try to load classes by name, it should avoid
     * class-loading issues.
     * @param clazz class to instantiate
     * @return the new instance
     * @throws BeanInstantiationException if the bean cannot be instantiated
     */
    private static <T> T instantiate(Class<T> clazz) throws BeanInstantiationException {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "Specified class is an interface");
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new BeanInstantiationException(clazz, "Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanInstantiationException(clazz, "Is the constructor accessible?", ex);
        }
    }

    private static void setValue(final Field field, final Object obj, final Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException ignored) {}
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the specified value type.
     * <p>Uses the specifically typed ResultSet accessor methods, falling back to
     * {@link #getResultSetValue(java.sql.ResultSet, String)} for unknown types.
     * <p>Note that the returned value may not be assignable to the specified
     * required type, in case of an unknown type. Calling code needs to deal
     * with this case appropriately, e.g. throwing a corresponding exception.
     *
     * @param rs           is the ResultSet holding the data
     * @param column       is the column name
     * @param requiredType the required value type (may be {@code null})
     * @return the value object
     * @throws SQLException if thrown by the JDBC API
     */
    @UsesJava7  // guard optional use of JDBC 4.1 (safe with 1.6 due to getObjectWithTypeAvailable check)
    private static Object getResultSetValue(ResultSet rs, String column, Class<?> requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, column);
        }
        Object value;
        // Explicitly extract typed value, as far as possible.
        if (String.class.equals(requiredType)) {
            return rs.getString(column);
        } else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
            value = rs.getBoolean(column);
        } else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
            value = rs.getByte(column);
        } else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
            value = rs.getShort(column);
        } else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
            value = rs.getInt(column);
        } else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
            value = rs.getLong(column);
        } else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
            value = rs.getFloat(column);
        } else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
                Number.class.equals(requiredType)) {
            value = rs.getDouble(column);
        } else if (BigDecimal.class.equals(requiredType)) {
            return rs.getBigDecimal(column);
        } else if (java.sql.Date.class.equals(requiredType)) {
            return rs.getDate(column);
        } else if (java.sql.Time.class.equals(requiredType)) {
            return rs.getTime(column);
        } else if (java.sql.Timestamp.class.equals(requiredType) || java.util.Date.class.equals(requiredType)) {
            return rs.getTimestamp(column);
        } else if (byte[].class.equals(requiredType)) {
            return rs.getBytes(column);
        } else if (Blob.class.equals(requiredType)) {
            return rs.getBlob(column);
        } else if (Clob.class.equals(requiredType)) {
            return rs.getClob(column);
        } else {
            return getResultSetValue(rs, column);
        }
        // Perform was-null check if necessary (for results that the JDBC driver returns as primitives).
        return (rs.wasNull() ? null : value);
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the most appropriate
     * value type. The returned value should be a detached value object, not having
     * any ties to the active ResultSet: in particular, it should not be a Blob or
     * Clob object but rather a byte array respectively String representation.
     * <p>Uses the {@code getObject(column)} method, but includes additional "hacks"
     * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
     * datatype and a {@code java.sql.Date} for DATE columns leaving out the
     * time portion: These columns will explicitly be extracted as standard
     * {@code java.sql.Timestamp} object.
     *
     * @param rs    is the ResultSet holding the data
     * @param column is the column column
     * @return the value object
     * @throws SQLException if thrown by the JDBC API
     * @see java.sql.Blob
     * @see java.sql.Clob
     * @see java.sql.Timestamp
     */
    private static Object getResultSetValue(ResultSet rs, String column) throws SQLException {
        Object obj = rs.getObject(column);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            obj = rs.getBytes(column);
        } else if (obj instanceof Clob) {
            obj = rs.getString(column);
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(column);
        } else if (className != null && className.startsWith("oracle.sql.DATE")) {
            obj = rs.getTimestamp(column);
        } else if (obj != null && obj instanceof java.sql.Date) {
            obj = rs.getTimestamp(column);
        }
        return obj;
    }
}
