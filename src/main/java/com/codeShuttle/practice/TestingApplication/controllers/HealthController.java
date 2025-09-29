package com.codeShuttle.practice.TestingApplication.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(path = "/")
    public ResponseEntity<String> getHealthCheck(){
            return new ResponseEntity<>("All ok", HttpStatus.OK);
    }

}
