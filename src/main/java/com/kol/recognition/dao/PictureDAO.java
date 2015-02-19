package com.kol.recognition.dao;

import com.kol.recognition.beans.DBImage;
import com.kol.recognition.beans.Image;
import com.kol.recognition.components.ImageManager;
import com.kol.recognition.interfaces.DBObject;
import com.kol.recognition.utils.NumberUtils;
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

    public <T extends DBObject<T, I>, I> T get(final I id, final Class<T> clazz) {
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
                "SELECT c.class_code AS clazz, rd.id " +
                "FROM recognition_data rd " +
                    "INNER JOIN recognition_data_class c ON c.class_code = rd.class_code " +
                "WHERE rd.image_width = ? AND rd.image_height = ? AND c.type_code = ? " +
                "ORDER BY c.class_code";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(DBImage.class), width, height, type);
    }

    public <T extends DBObject<T, Integer>> void save(final DBObject<T, Integer> object) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert((JdbcTemplate) jdbc)
                .withTableName(object.getTableName())
                .usingGeneratedKeyColumns(object.getIdentityFieldName());
        final Map<String, Object> parameters = object.toSimpleJdbcInsertParams();
        parameters.put("create_by", MDC.get("user"));
        parameters.put("edit_by", MDC.get("user"));
        parameters.put("create_date", new Date());
        parameters.put("edit_date", new Date());
        object.setId(insert.executeAndReturnKey(parameters).intValue());
    }

    public void save(final Collection<DBImage> list) {
        list.forEach(this::save);
    }

    public Collection<String> getClasses(final String type) {
        final String sql =
                "SELECT rdc.class_code FROM recognition_data_class rdc " +
                "WHERE rdc.type_code = ? AND rdc.class_code NOT IN (?, ?)";
        return jdbc.queryForList(sql, String.class, type, ImageManager.IMAGE_CLASS_CROPPED_CODE, ImageManager.IMAGE_CLASS_UPLOADED_CODE);
    }

    public Collection<Image> getImages(final String typeCode, final String classCode, final int width, final int height) {
        final String sql = "SELECT rd.*, c.class_code FROM recognition_data rd " +
                "INNER JOIN recognition_data_class c ON c.class_code = rd.class_code " +
                "WHERE c.type_code = ? AND rd.class_code = ? AND rd.image_width = ? AND image_height = ?";
        return jdbc.query(sql, (rs, i) -> {
            final Image image = new Image();
            image.setSize(rs.getInt("image_size"));
            image.setWidth(rs.getInt("image_width"));
            image.setHeight(rs.getInt("image_height"));
            image.setClazz(rs.getString("class_code"));
            image.setFormat(rs.getString("image_format"));
            image.setId(NumberUtils.encode(rs.getInt("id")));
            final String content = rs.getString("image_content");
            image.setContent(Base64.getDecoder().decode(content.getBytes()));
            return image;
        }, typeCode, classCode, width, height);
    }
}
