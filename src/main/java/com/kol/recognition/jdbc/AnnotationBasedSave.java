package com.kol.recognition.jdbc;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kol.recognition.beans.entities.HistoryObject;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationBasedSave<T extends HistoryObject> {

    private Class<T> mappedClass;

    private String table;
    private List<String> insertColumns;
    private List<String> updateColumns;
    private Map<String, Field> fieldMapping;

    private static final String ID = "id";
    private static final String CREATE_BY = "create_by";
    private static final String CREATE_DATE = "create_date";

    public AnnotationBasedSave(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.table = tableName();
        fieldMapping();
    }

    public String saveSQL() {
        return "INSERT INTO " + table + insertColumns.stream().collect(Collectors.joining(",", " (", ") ")) + "VALUES(?" + Strings.repeat(",?", fieldMapping.values().size() - 1) + ")";
    }

    public String updateSQL() {
        return "UPDATE " + table + " SET " + updateColumns.stream().map(v -> v + " = ?").collect(Collectors.joining(",")) + " WHERE id = ?";
    }

    public Object[] saveParameters(final T obj) {
        final Object[] parameters = new Object[insertColumns.size()];
        for (int i = 0; i < insertColumns.size(); i++) {
            try {
                final Field field = fieldMapping.get(insertColumns.get(i));
                field.setAccessible(true);
                parameters[i] = field.get(obj);
            } catch (IllegalAccessException e) {
                parameters[i] = null;
            }
        }
        return parameters;
    }

    public Object[] updateParameters(final T obj) {
        final Object[] parameters = new Object[updateColumns.size() - 2];
        for (int i = 0; i < updateColumns.size(); i++) {
            try {
                parameters[i] = fieldMapping.get(updateColumns.get(i)).get(obj);
            } catch (IllegalAccessException e) {
                parameters[i] = null;
            }
        }
        parameters[updateColumns.size() - 3] = obj.getId();
        return parameters;
    }

    private String tableName() {
        final Table annotation = mappedClass.getAnnotation(Table.class);
        if(null == annotation) {
            throw new RuntimeException("Not mapped class");
        } else {
            return annotation.name();
        }
    }

    private void fieldMapping() {
        fieldMapping = new HashMap<>();
        ReflectionUtils.doWithFields(mappedClass, field -> {
            final Column annotation = field.getAnnotation(Column.class);
            if(null != annotation) {
                fieldMapping.put(annotation.name(), field);
            }
        });
        insertColumns = fieldMapping.keySet().stream().sorted().collect(Collectors.toList());
        updateColumns = insertColumns.stream().filter(v -> !ID.equals(v) && !CREATE_BY.equals(v) && !CREATE_DATE.equals(v)).sorted().collect(Collectors.toList());
    }
}
