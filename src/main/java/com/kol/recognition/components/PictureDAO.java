package com.kol.recognition.components;

import com.google.common.base.Strings;
import com.kol.recognition.beans.ImageBean;
import com.kol.recognition.beans.entities.DBImage;
import com.kol.recognition.beans.entities.HistoryObject;
import com.kol.recognition.jdbc.AnnotationBasedSave;
import com.kol.recognition.jdbc.AnnotationRowMapper;
import com.kol.recognition.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import javax.persistence.Table;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.kol.recognition.beans.entities.RecognitionDataClass.IMAGE_CLASS_CROPPED_CODE;
import static com.kol.recognition.beans.entities.RecognitionDataClass.IMAGE_CLASS_UPLOADED_CODE;

@Component
public class PictureDAO {
    @Autowired private JdbcOperations jdbc;

    public <T extends HistoryObject> T get(final String id, final Class<T> clazz) {
        final Table table = clazz.getAnnotation(Table.class);
        final String sql = "SELECT * FROM " + table.name() + " WHERE id = ?";
        return jdbc.queryForObject(sql, new AnnotationRowMapper<>(clazz), id);
    }

    public <T extends HistoryObject> void save(final T obj, final String username, final Class<T> clazz) {
        final AnnotationBasedSave<T> abs = new AnnotationBasedSave<>(clazz);
        if(Strings.isNullOrEmpty(obj.getId())) {
            obj.setId(NumberUtils.generateId());
            obj.setCreateBy(username);
            obj.setCreateDate(new Date());
            obj.setEditBy(username);
            obj.setEditDate(new Date());
            jdbc.update(abs.saveSQL(), abs.saveParameters(obj));
        } else {
            obj.setEditBy(username);
            obj.setEditDate(new Date());
            jdbc.update(abs.updateSQL(), abs.updateParameters(obj));
        }
    }

    public DBImage getImage(final String id) {
        return get(id, DBImage.class);
    }

    public void saveImage(final DBImage image, final String username) {
        save(image, username, DBImage.class);
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
}
