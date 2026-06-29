package com.main.personalprojectback.service;

import com.main.personalprojectback.dao.MainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
    @Autowired
    MainDao dao;

    public String tester() {

        String result = dao.tester();
        return result;
    }
}
