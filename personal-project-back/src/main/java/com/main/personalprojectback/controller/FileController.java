package com.main.personalprojectback.controller;

import com.main.personalprojectback.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/file")
@RestController
@CrossOrigin("http://localhost:5173")
public class FileController {
    @Autowired
    FileService service;
    @GetMapping("/test")
    public ResponseEntity<?> testTest (){
        String result = service.testTest();
        return ResponseEntity.ok(result);
    }
}
