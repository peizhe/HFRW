package com.kol.recognition.dao;

import com.kol.recognition.beans.DBImage;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Component
public class PictureDAO {
    @Autowired private JdbcOperations jdbc;

    private static <T> T getItem(final List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    public DBImage getImage(final int id) {
        final String sql =
                "SELECT `image_format`, `image_content`, `image_size`, `image_width`, `image_height` " +
                "FROM `recognition_data` WHERE `id` = ?";
        return jdbc.queryForObject(sql, (rs, i) -> {
            final DBImage image = new DBImage();
            image.setFormat(rs.getString("image_format"));
            image.setContent(rs.getBytes("image_content"));
            image.setSize(rs.getInt("image_size"));
            image.setWidth(rs.getInt("image_width"));
            image.setHeight(rs.getInt("image_height"));
            image.setId(id);
            return image;
        }, id);
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

    public int createImage(final DBImage image) {
        final String sql = "INSERT INTO recognition_data " +
                "(class_code, image_format, image_width, image_height, image_size, image_content, create_date, create_by, edit_date, edit_by, parent_image_id)" +
                "VALUES(:class_code,:image_format,:image_width,:image_height,:image_size,:image_content,NOW(),:create_by,NOW(),:edit_by,:parent_image_id)";

        final SimpleJdbcInsert insert = new SimpleJdbcInsert((JdbcTemplate) jdbc).withTableName("recognition_data").usingGeneratedKeyColumns("id");
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("class_code", image.getClazz());
        parameters.put("image_format", image.getFormat());
        parameters.put("image_width", image.getWidth());
        parameters.put("image_height", image.getHeight());
        parameters.put("image_size", image.getSize());
        parameters.put("image_content", new String(image.getContent()));
        parameters.put("create_by", MDC.get("user"));
        parameters.put("edit_by", MDC.get("user"));
        parameters.put("create_date", new Date());
        parameters.put("edit_date", new Date());
        parameters.put("parent_image_id", image.getParentId());
        return insert.executeAndReturnKey(parameters).intValue();
    }
}
