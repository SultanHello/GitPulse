package org.example.emailsendergitservice.services;

import lombok.AllArgsConstructor;
import org.example.emailsendergitservice.model.Starter;
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
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;


    public void sendEmail(Starter starter,String text) {
        System.out.println(starter.getToken());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + starter.getToken());

        org.springframework.http.HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://USERGITSERVICE/users/getEmail",
                HttpMethod.GET,
                entity,
                String.class
        );

        String to = response.getBody();
        String subject = "GitPulse";



        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("asimbek06@mail.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        System.out.println(1);
        System.out.println(to);
        mailSender.send(message);
    }

}