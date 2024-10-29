package org.example.releasegitservice.connectionService;

import lombok.AllArgsConstructor;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.services.CalligraphyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class NotificationConnection {
    @Autowired
    private KafkaTemplate<String, Map<String,String>> kafkaTemplate1;

    private static final Logger logger = LoggerFactory.getLogger(NotificationConnection.class);

    private final CalligraphyService calligraphyService;

    public void sendMessage(Release release, Starter starter, String authHeader){
        String string=calligraphyService.view(release);
        try {

            if (string == null) {
                logger.error("Generated message string is null for release: {}", release);
                return;
            }
            starter.setToken(authHeader.substring(7));
            logger.info("starting connecting with notification for send email message : {}",string);
            starter.setMessage(string);

            Map<String, String> map = new HashMap<>();
            map.put("webhoockUrl", starter.getWebhoockUrl());
            map.put("repoName", starter.getRepoName());
            map.put("token", starter.getToken());
            map.put("message", starter.getMessage());
            logger.info("starting send to email");
            kafkaTemplate1.send("my-topic-email", map);
            logger.info("starting send to slack");
            kafkaTemplate1.send("my-topic-slack", map);
        }catch (Exception e){
            logger.error("error while connect with notification service",e);
            throw new RuntimeException("error when connect");
        }


    }


}
