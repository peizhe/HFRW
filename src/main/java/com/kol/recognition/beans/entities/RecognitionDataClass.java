package com.kol.recognition.beans.entities;

import javax.persistence.Table;
import javax.persistence.Column;

@Table(name = "recognition_data_class")
public class RecognitionDataClass extends SupportObject {

    public static final String IMAGE_CLASS_CROPPED_CODE = "CRPD";
    public static final String IMAGE_CLASS_UPLOADED_CODE = "UPLD";

    @Column(name = "type_code")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
