package com.kol.recognition.dao;

import com.kol.recognition.beans.DBImage;
import com.kol.recognition.interfaces.DBObject;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
public class PictureDAO {
    @Autowired private JdbcOperations jdbc;

    private static <T> T getFirst(final List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    public <T extends DBObject<T>> T get(final int id, final Class<T> clazz) {
        final T t;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {
            return null;
        }
        final String sql = "SELECT * FROM " + t.getTableName() + " WHERE " + t.getIdentityFieldName() + " = ?";
        return getFirst(jdbc.query(sql, t.getRowMapper(), id));
    }

    public List<DBImage> getImages(final int width, final int height, final String type) {
        final String sql =
                "SELECT c.name, rd.id " +
                "FROM recognition_data rd " +
                    "INNER JOIN recognition_data_class c ON c.class_code = rd.class_code " +
                "WHERE rd.image_width = ? AND rd.image_height = ? AND c.type_code = ? " +
                "ORDER BY c.name";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(DBImage.class), width, height, type);
    }

    public <T extends DBObject<T>> void save(final DBObject<T> object) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert((JdbcTemplate) jdbc)
                .withTableName(object.getTableName())
                .usingGeneratedKeyColumns(object.getIdentityFieldName());
        final Map<String, Object> parameters = object.toSimpleJdbcInsertParams();
        parameters.put("create_by", MDC.get("user"));
        parameters.put("edit_by", MDC.get("user"));
        parameters.put("create_date", new Date());
        parameters.put("edit_date", new Date());
        object.setIdentifier(insert.executeAndReturnKey(parameters).intValue());
    }

    public void save(final Collection<DBImage> list) {
        list.forEach(this::save);
    }
}
