package com.kol.recognition.dao;

import com.kol.recognition.beans.DBImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
public class PictureDAO {
    @Autowired private JdbcOperations jdbc;

    private static <T> T getItem(final List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    public DBImage getImage(final int id) {
        final String sql = "SELECT `format`, `content`, `size` FROM recognition_data WHERE id = ?";
        return getItem(jdbc.query(sql, (rs, i) -> {
            final DBImage image = new DBImage();
            image.setFormat(rs.getString("format"));
            image.setSize(rs.getInt("size"));
            image.setContent(Base64.getDecoder().decode(rs.getString("content")));
            return image;
        }, id));
    }

    public List<DBImage> getImages(final String type, final int width, final int height) {
        final String sql =
                "SELECT c.name, rd.id " +
                "FROM recognition_data rd " +
                    "INNER JOIN recognition_data_class c ON c.class_code = rd.class " +
                "WHERE rd.type = ? AND rd.width = ? AND rd.height = ? " +
                "ORDER BY c.name";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(DBImage.class), type, width, height);
    }
}
