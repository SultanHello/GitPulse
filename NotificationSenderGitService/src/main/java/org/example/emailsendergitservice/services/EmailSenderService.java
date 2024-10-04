package org.example.emailsendergitservice.services;

import lombok.AllArgsConstructor;
import org.example.emailsendergitservice.controllers.EmailSenderController;
import org.example.emailsendergitservice.model.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderController.class);
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;


    public void sendEmail(Starter starter,String text) {
        String to;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + starter.getToken());
            org.springframework.http.HttpEntity<String> entity = new HttpEntity<>(headers);
            logger.info("starting connect with user microservice for get email");
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

            logger.info("add data to message from : {} ,to : {} ,text : {}","asimbek06@mail.ru",to,text);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("asimbek06@mail.ru");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            logger.info("starting send message : {}",message);
            mailSender.send(message);
        }catch (Exception e){
            logger.error("error with Java Mail Sender",e);
            throw new RuntimeException("error with Java Mail Sender");
        }

    }

}