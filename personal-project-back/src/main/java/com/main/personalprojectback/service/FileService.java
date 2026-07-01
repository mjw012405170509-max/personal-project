package com.main.personalprojectback.service;

import com.main.personalprojectback.dao.FileDao;
import com.main.personalprojectback.global.util.PdfConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {
   @Autowired
    FileDao dao;
   @Autowired
    PdfConverter converter;

    public String testTest() {
        String result = dao.testTest();
        return result;
    }

    public Object inspect(MultipartFile file, String fileType) throws IOException {
        String das="still";
        if(fileType.equals("application/pdf")){
            //pdf이미지 변환 + 이미지 처리
            List<BufferedImage> images = converter.pdfToImg(file);
        }
        else if(fileType.startsWith("image/")){
            //이미지 처리
        }
        return das;
    }
}
