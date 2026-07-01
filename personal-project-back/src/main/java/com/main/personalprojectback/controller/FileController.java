package com.main.personalprojectback.controller;

import com.main.personalprojectback.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PostMapping("inspect")
    public ResponseEntity<?> inspect(@RequestParam MultipartFile file) throws IOException {
        String fileType = file.getContentType();
        if(file.isEmpty() || fileType==null){
            return ResponseEntity.badRequest().body("파일의 형식이 확인되지 않습니다.");
        }
        Object result = service.inspect(file,fileType);
        return ResponseEntity.ok(null);
    }
}
