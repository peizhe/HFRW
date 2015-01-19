package com.kol.recognition.web.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

@Component
public class PictureDAO {
    @Autowired private JdbcOperations jdbc;

    public void loadImage(final int id) {
        final String sql = "SELECT format, content, size FROM recognition_data WHERE id = ?";

    }
}
