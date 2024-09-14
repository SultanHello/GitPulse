package org.example.usergitservice.controllers;


import lombok.AllArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.usergitservice.models.LogUser;
import org.example.usergitservice.models.RegUser;
import org.example.usergitservice.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/login")
    public String login(@RequestBody LogUser logUser){
        return userService.login(logUser);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegUser regUser){
        return userService.register(regUser);
    }

    @GetMapping("/getUser")
    public String getUser(@RequestParam String token){
        return userService.getGitUsernameByEmail(token);
    }


}
