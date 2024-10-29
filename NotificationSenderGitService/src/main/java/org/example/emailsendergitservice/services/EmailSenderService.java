package org.example.emailsendergitservice.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service

@RequiredArgsConstructor


public class EmailSenderService {
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;
    Map<String ,String > map;

    @KafkaListener(topics = "my-topic-email", groupId = "my-group2")
    public void listenToObjectMessage(Map<String,String> map) {
        logger.info("SULAGTA");
        this.map=map;
        sendEmail();
    }
    public void sendEmail() {
        if (map == null) {
            logger.error("Starter or text is null. Cannot send email.");
            return;
        }

        String to;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " +map.get("token"));
            org.springframework.http.HttpEntity<String> entity = new HttpEntity<>(headers);
            logger.info("starting connect with user microservice for get email");
            logger.info("gtaGTA");
            ResponseEntity<String> response= restTemplate.exchange(
                    "http://USERGITSERVICE/users/getEmail",
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            to = response.getBody();

        }catch (Exception e){
            logger.error("error while connect user microservice");
            throw new RuntimeException("error while connect user microservice");

        }
        try {
            logger.info("preparing variables for send message");

            String subject = "GitPulse";

            logger.info("add data to message from : {} ,to : {} ,text : {}","asimbek06@mail.ru",to,map.get("message"));
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("asimbek06@mail.ru");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(map.get("message"));

            logger.info("starting send message : {}",message);
            mailSender.send(message);
        }catch (Exception e){
            logger.error("error with Java Mail Sender",e);
            throw new RuntimeException("error with Java Mail Sender");
        }

    }

}