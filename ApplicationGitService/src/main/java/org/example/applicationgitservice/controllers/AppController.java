package org.example.applicationgitservice.controllers;

import lombok.AllArgsConstructor;
import org.example.applicationgitservice.models.Starter;
import org.example.applicationgitservice.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
@AllArgsConstructor
public class AppController {
    @Autowired
    private final AppService appService;
    @GetMapping("/start")
    public String start(@RequestBody Starter starter, @RequestHeader(value = "Authorization") String authHeader){
        System.out.println(1);
        return appService.start(starter,authHeader);
    }
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

}
