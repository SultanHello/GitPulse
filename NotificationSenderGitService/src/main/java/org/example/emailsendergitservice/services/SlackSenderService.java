package org.example.emailsendergitservice.services;

import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.emailsendergitservice.controllers.EmailSenderController;
import org.example.emailsendergitservice.model.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service

public class SlackSenderService {

    private static final Logger logger = LoggerFactory.getLogger(SlackSenderService.class);


    public String sendNotification(String message, Starter starter) {
        String webhookUrl = starter.getWebhoockUrl();

        logger.info("formatting JSON  with message : {}",message);
        String json = "{\"text\":\"" + message + "\"}";


        logger.info("creating headers");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(json, headers);
        RestTemplate restTemplate=new RestTemplate();

        try {
            logger.info("starting send post response where url {}",webhookUrl);

            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, entity, String.class);
            logger.info("checking status of response : {}",response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Notification sent successfully ,sent message : {} ",message);
                logger.info("Response : {}",response.getBody());
                return "success send slack";

            } else {
                logger.error("Failed to send notification. Status: {}",response.getStatusCode());
                logger.error("Response : {}",response.getBody());
                return "error send slack";

            }
        } catch (Exception e) {
            logger.error("An error occurred while sending the notification: {}",e.getMessage());
            throw new RuntimeException("An error occurred while sending the notification");

        }
    }
}
