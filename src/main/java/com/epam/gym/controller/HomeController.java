package com.epam.gym.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class HomeController {

    @GetMapping
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Welcome to Gym API", HttpStatus.OK);
    }
}