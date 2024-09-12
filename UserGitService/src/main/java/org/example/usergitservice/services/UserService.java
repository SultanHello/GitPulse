package org.example.usergitservice.services;

import lombok.AllArgsConstructor;
import org.example.usergitservice.filter.JwtAuthenticationFilter;
import org.example.usergitservice.models.RegUser;
import org.example.usergitservice.models.User;
import org.example.usergitservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    public String register(RegUser regUser){

        User userToken =userRepository.save(
                User.builder()
                        .email(regUser.getEmail())
                        .password(regUser.getPassword())
                        .gitUsername(regUser.getGitUsername())
                        .build()
        );
        return jwtService.generateToken(userToken);


    }
}
