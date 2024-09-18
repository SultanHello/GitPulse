package org.example.releasegitservice.controllers;


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
//    @PostMapping("/addAllRepo")
//    public String addAllReleases(@RequestParam String repoName,@RequestHeader(value = "Authorization") String authHeader){
//        releaseService.addAllReleases(repoName,authHeader.trim());
//        return "success";
//    }
    @PostMapping("/addRepo")
    public String addReleases(@RequestBody Starter starter, @RequestHeader(value = "Authorization") String authHeader){
        System.out.println(3);
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


//    @GetMapping("/check")
//    public String check(){
//        releaseService.
//    }
    @GetMapping
    public List<Release> releases(){
        return releaseService.allReleases();
    }





}
