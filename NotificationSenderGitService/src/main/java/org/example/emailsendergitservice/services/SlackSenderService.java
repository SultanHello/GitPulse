package org.example.emailsendergitservice.services;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service


public class SlackSenderService {

    private static final Logger logger = LoggerFactory.getLogger(SlackSenderService.class);
    Map<String ,String > map;




    @KafkaListener(topics = "my-topic-slack", groupId = "my-group4")
    public void listenToObjectMessage(Map<String,String> map) {
        logger.info("SULAGTA");
        this.map=map;
        sendNotification();
    }

    public String sendNotification() {
        if (map == null) {
            logger.error("Starter or text is null. Cannot send email.");
            return null;
        }
        String webhookUrl =map.get("webhoockUrl");

        logger.info("formatting JSON  with message : {}",map.get("message"));
        String json = "{\"text\":\"" + map.get("message") + "\"}";


        logger.info("creating headers");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        RestTemplate restTemplate=new RestTemplate();

        try {
            logger.info("starting send post response where url {}",webhookUrl);

            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, entity, String.class);
            logger.info("checking status of response : {}",response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("Notification sent successfully ,sent message : {} ",map.get("message"));
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
