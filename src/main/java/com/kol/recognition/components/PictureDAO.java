package com.kol.recognition.components;

import com.google.common.base.Strings;
import com.kol.recognition.jdbc.AnnotationBasedSave;
import com.kol.recognition.jdbc.AnnotationRowMapper;
import com.kol.recognition.beans.ImageBean;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.beans.entities.RecognitionDataClass;
import com.kol.recognition.beans.entities.User;
import com.kol.recognition.service.ImageService;
import com.kol.recognition.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;
import static com.kol.recognition.beans.entities.RecognitionDataClass.*;

import java.util.*;

@Component
public class PictureDAO {
    @Autowired private JdbcOperations jdbc;
    @Autowired private ImageService imageService;

    public DBImage getImage(final String id) {
        final String sql =
                "SELECT " +
                    "id, class_code, image_size, image_format, " +
                    "image_width, image_height, image_content, parent_image_id, " +
                    "edit_by, edit_date, create_by, create_date " +
                "FROM recognition_data WHERE id = ?";
//        return jdbc.queryForObject(sql, new AnnotationRowMapper<>(DBImage.class), id);
        return null;
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

    public void saveImage(final DBImage image, final String username) {
//        final AnnotationBasedSave<DBImage> abs = new AnnotationBasedSave<>(DBImage.class);
//        if(Strings.isNullOrEmpty(image.getId())) {
//            image.setId(NumberUtils.generateId());
//            image.setCreateBy(username);
//            image.setCreateDate(new Date());
//            image.setEditBy(username);
//            image.setEditDate(new Date());
//            jdbc.update(abs.saveSQL(), abs.saveParameters(image));
//        } else {
//            image.setEditBy(username);
//            image.setEditDate(new Date());
//            jdbc.update(abs.updateSQL(), abs.updateParameters(image));
//        }
    }

    public void saveImages(final Collection<DBImage> list, final String username) {
        list.forEach(i -> saveImage(i, username));
    }

    public Collection<String> getClasses(final String type) {
        final String sql = "SELECT rdc.code FROM recognition_data_class rdc WHERE rdc.type_code = ? AND rdc.code NOT IN (?, ?)";
        return jdbc.queryForList(sql, String.class, type, IMAGE_CLASS_CROPPED_CODE, IMAGE_CLASS_UPLOADED_CODE);
    }

    public Collection<DBImage> getImages(final String classCode, final int width, final int height) {
        final String sql =
                "SELECT rd.* FROM recognition_data rd " +
                "WHERE rd.class_code = ? AND rd.image_width = ? AND rd.image_height = ?";
        return jdbc.query(sql, new AnnotationRowMapper<>(DBImage.class), classCode, width, height);
    }

    public RecognitionDataClass getClassByCode(final String code) {
        final String sql = "SELECT * FROM recognition_data_class WHERE code = ?";
        return jdbc.queryForObject(sql, new AnnotationRowMapper<>(RecognitionDataClass.class), code);
    }

    public User getUser(final String username) {
        final String sql = "SELECT * FROM users WHERE username = ?";
        return jdbc.queryForObject(sql, new AnnotationRowMapper<>(User.class), username);
    }
}
