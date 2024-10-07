package org.example.usergitservice.services;

import lombok.AllArgsConstructor;
import org.example.usergitservice.controllers.UserController;
import org.example.usergitservice.filter.JwtAuthenticationFilter;
import org.example.usergitservice.models.LogUser;
import org.example.usergitservice.models.RegUser;
import org.example.usergitservice.models.User;
import org.example.usergitservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired

    private final AuthenticationManager manager;
    public String getGitUsernameByEmail(String token){
        try {
            logger.info("starting work with JwtService");
            String emailFromJwt =jwtService.extractUsername(token);
            logger.info("starting finding User by email : {}",emailFromJwt);
            User user =userRepository.findByEmail(emailFromJwt);
            if (user == null) {
                logger.error("no user found with email: {}", emailFromJwt);
                throw new RuntimeException("user not found for email: " + emailFromJwt);
            }
            return user.getGitUsername();
        }catch (Exception e){
            logger.error("error while connection with other classes",e);
            throw new RuntimeException("error while connection with other classes");
        }
    }
    public String register(RegUser regUser){
        try {
            logger.info("starting check to is {} this Identity user",regUser.getEmail());
            if(userRepository.existsByEmail(regUser.getEmail())&&userRepository.existsByGitUsername(regUser.getGitUsername())){
                logger.error("user with this email or git username already exist");
                throw new RuntimeException("user with this email or git username already exist");
            }
            
            logger.info("starting save authenticated user : {}",regUser);
            User user =userRepository.save(
                    User.builder()
                            .email(regUser.getEmail())
                            .password(encoder.encode(regUser.getPassword()))
                            .gitUsername(regUser.getGitUsername())
                            .build()
            );
            logger.info("saved data {} for future use",user);
            logger.info("starting generate token by user {} ,then return it",user);
            return jwtService.generateToken(user);
        }catch (Exception e){
            logger.error("error while saving data {}",regUser,e);
            throw new RuntimeException("error while saving data");

        }

    }
    public String login(LogUser logUser){
        if(!userRepository.existsByEmail(logUser.getEmail())){
            logger.error("not exist user with this email : {}",logUser.getEmail());
            throw new RuntimeException("not exist user with this email");
        }
        try {
            logger.info("starting authenticate");
            manager.authenticate(new UsernamePasswordAuthenticationToken(logUser.getEmail(),logUser.getPassword()));
            logger.info("starting get user by email : {}",logUser.getEmail());
            User user = userRepository.findByEmail(logUser.getEmail());
            logger.info("starting generate token by user {} for return",user);
            return jwtService.generateToken(user);

        }catch (Exception e){
            logger.error("error while authenticate user : {}",logUser,e);
            throw new RuntimeException("error while authenticate ");
        }

    }
    public String getEmail(String token){
        logger.info("starting get and check email");
        String emailFromJwt = jwtService.extractUsername(token);
        if(emailFromJwt==null){
            throw new RuntimeException("email extracted from JWT is null");
        }
        logger.info("starting get and check user");
        User user = userRepository.findByEmail(emailFromJwt);
        if (user == null) {
            throw new RuntimeException("user not found with email: " + emailFromJwt);
        }

        return user.getEmail();
    }
}
