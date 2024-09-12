package org.example.usergitservice.controllers;


import lombok.AllArgsConstructor;
import org.example.usergitservice.models.RegUser;
import org.example.usergitservice.services.UserService;
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

    @PostMapping("/register")
    public String register(@RequestBody RegUser regUser){
        return userService.register(regUser);
    }


}
