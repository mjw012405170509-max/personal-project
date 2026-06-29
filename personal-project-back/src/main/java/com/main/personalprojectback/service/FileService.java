package com.main.personalprojectback.service;

import com.main.personalprojectback.dao.FileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
   @Autowired
    FileDao dao;

    public String testTest() {
        String result = dao.testTest();
        return result;
    }
}
