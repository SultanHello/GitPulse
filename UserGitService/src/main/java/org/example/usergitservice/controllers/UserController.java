package org.example.usergitservice.controllers;


import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.usergitservice.models.LogUser;
import org.example.usergitservice.models.RegUser;
import org.example.usergitservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/login")
    public String login(@RequestBody LogUser logUser){
        logger.info("starting login with data {}",logUser);
        return userService.login(logUser);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegUser regUser){
        logger.info("starting register with data {}",regUser);
        return userService.register(regUser);
    }

    @GetMapping("/getGitUsername")
    public String getUser(@RequestParam String token){
        logger.info("starting getting user by token : {}",token);
        return userService.getGitUsernameByEmail(token);
    }

    @GetMapping("/getEmail")
    public String getEmail(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        logger.info("starting getting email by token : {}",token);
        return userService.getEmail(token);
    }


}
