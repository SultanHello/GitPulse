package org.example.releasegitservice.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.services.ReleaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/releases")
@AllArgsConstructor
public class ReleaseController {
    private  final ReleaseService releaseService;
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/addRepo")
    public String addReleases(@RequestBody Starter starter, @RequestHeader(value = "Authorization") String authHeader){
        releaseService.addReleases(starter,authHeader.trim());
        return "success";
    }
    @GetMapping("/getRepos")
    public List<String> getReposNames(@RequestHeader(value = "Authorization") String authHeader){
        return releaseService.getReposNames(authHeader);

    }
    @GetMapping("/getReposReleases/{reposName}")
    public List<Release> getReposReleases(@RequestHeader(value = "Authorization") String authHeader,@PathVariable String reposName){
        return releaseService.getReposReleases(authHeader,reposName);
    }

    @GetMapping
    public List<Release> releases(){
        return releaseService.allReleases();
    }





}
