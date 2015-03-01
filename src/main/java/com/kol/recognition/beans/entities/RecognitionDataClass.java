package com.kol.recognition.beans.entities;

import javax.persistence.*;

@Entity
@Table(name = "recognition_data_class")
public class RecognitionDataClass extends SupportObject {

    public static final String IMAGE_CLASS_CROPPED_CODE = "CRPD";
    public static final String IMAGE_CLASS_UPLOADED_CODE = "UPLD";

    private RecognitionDataType type;

    @ManyToOne
    @JoinColumn(name = "type_code")
    public RecognitionDataType getType() {
        return type;
    }

    public void setType(RecognitionDataType type) {
        this.type = type;
    }
}
