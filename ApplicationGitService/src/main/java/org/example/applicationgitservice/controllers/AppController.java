package org.example.applicationgitservice.controllers;

import lombok.AllArgsConstructor;
import org.example.applicationgitservice.models.Starter;
import org.example.applicationgitservice.services.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
@AllArgsConstructor
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private final AppService appService;
    @GetMapping("/start")
    public void start(@RequestBody Starter starter, @RequestHeader(value = "Authorization") String authHeader){
        logger.info("Application start");
        appService.start(starter,authHeader);
    }

}
