package com.main.personalprojectback.controller;

import com.main.personalprojectback.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/main")
@CrossOrigin("*")
public class MainController {
    @Autowired
    MainService service;
    @GetMapping("/test")
    public ResponseEntity<?> tester(){

        String result = service.tester();


        return ResponseEntity.ok(result);
    }

}
