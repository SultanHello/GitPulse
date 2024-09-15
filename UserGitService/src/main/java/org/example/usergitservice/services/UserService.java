package org.example.usergitservice.services;

import lombok.AllArgsConstructor;
import org.example.usergitservice.filter.JwtAuthenticationFilter;
import org.example.usergitservice.models.LogUser;
import org.example.usergitservice.models.RegUser;
import org.example.usergitservice.models.User;
import org.example.usergitservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired

    private final AuthenticationManager manager;
    public String getGitUsernameByEmail(String token){
        return userRepository.findByEmail(jwtService.extractUsername(token)).getGitUsername();

    }
    public String register(RegUser regUser){



        User userToken =userRepository.save(
                User.builder()
                        .email(regUser.getEmail())
                        .password(encoder.encode(regUser.getPassword()))
                        .gitUsername(regUser.getGitUsername())
                        .build()
        );
        return jwtService.generateToken(userToken);


    }
    public String login(LogUser logUser){
        System.out.println(2);
        manager.authenticate(new UsernamePasswordAuthenticationToken(logUser.getEmail(),logUser.getPassword()));
        User user = userRepository.findByEmail(logUser.getEmail());
        return jwtService.generateToken(user);
    }
}
