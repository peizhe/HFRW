package com.kol.recognition.service;

import com.kol.recognition.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ImageService {
    @Resource public ImageRepository imageRepository;
}
