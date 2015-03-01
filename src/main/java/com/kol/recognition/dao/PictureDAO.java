package com.kol.recognition.dao;

import com.kol.recognition.beans.ImageBean;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.beans.Image;
import com.kol.recognition.beans.entities.RecognitionDataClass;
import com.kol.recognition.beans.entities.User;
import com.kol.recognition.services.HibernateService;
import com.kol.recognition.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PictureDAO {
    @Autowired private JdbcOperations jdbc;
    @Autowired private HibernateService service;

    public DBImage getImage(final Integer id) {
        return service.get(DBImage.class, id);
    }

    public List<ImageBean> getImages(final int width, final int height, final String type) {
        final String sql =
                "SELECT c.code AS imageClass, rd.id AS imageId " +
                "FROM recognition_data rd " +
                    "INNER JOIN recognition_data_class c ON c.code = rd.class_code " +
                "WHERE rd.image_width = ? AND rd.image_height = ? AND c.type_code = ? " +
                "ORDER BY c.code";
        return jdbc.query(sql, new BeanPropertyRowMapper<>(ImageBean.class), width, height, type);
    }

    public void saveImage(final DBImage image) {
        service.save(image);
    }

    public void saveImages(final Collection<DBImage> list) {
        service.save(list);
    }

    public Collection<String> getClasses(final String type) {
        final String sql = "SELECT rdc.code FROM recognition_data_class rdc WHERE rdc.type_code = ? AND rdc.code NOT IN (?, ?)";
        return jdbc.queryForList(sql, String.class, type, RecognitionDataClass.IMAGE_CLASS_CROPPED_CODE, RecognitionDataClass.IMAGE_CLASS_UPLOADED_CODE);
    }

    public Collection<Image> getImages(final String typeCode, final String classCode, final int width, final int height) {
        final String sql = "SELECT rd.*, c.code FROM recognition_data rd " +
                "INNER JOIN recognition_data_class c ON c.code = rd.class_code " +
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

    public RecognitionDataClass getClassByCode(final String code) {
        return service.getByCode(RecognitionDataClass.class, code);
    }

    public User getUser(final String username) {
        return service.getUser(username);
    }
}
