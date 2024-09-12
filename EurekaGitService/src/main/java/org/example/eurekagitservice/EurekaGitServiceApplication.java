package org.example.eurekagitservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaGitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaGitServiceApplication.class, args);
    }

}
