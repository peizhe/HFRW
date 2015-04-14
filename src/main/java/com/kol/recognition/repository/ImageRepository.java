package com.kol.recognition.repository;

import com.kol.recognition.beans.entities.DBImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<DBImage, String> {
}
