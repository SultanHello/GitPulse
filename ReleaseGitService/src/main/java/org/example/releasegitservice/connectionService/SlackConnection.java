package org.example.releasegitservice.connectionService;


import lombok.AllArgsConstructor;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.services.CalligraphyService;
import org.example.releasegitservice.services.ReleaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class SlackConnection {
    List<Release> messages ;
    private static final Logger logger = LoggerFactory.getLogger(EmailConnection.class);


    private final RestTemplate restTemplate;
    private final CalligraphyService calligraphyService;
    public void sendMessage(Release release, Starter starter){
        try{
            String string=calligraphyService.view(release);
            if (string == null) {
                logger.error("Generated message string is null for release: {}", release);
                return;
            }
            logger.info("starting connecting with notification for send to slack message : {}",string);

            ResponseEntity<Void> response=restTemplate.postForEntity(
                    "http://NOTIFICATIONSENDERGITSERVICE/notification/sendSlack?message="+string,
                    starter,
                    Void.class
            );
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Failed to send slack notification, status code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to send slack notification");
            }
            logger.info("success sent slack message : {}",string);

        }catch (Exception e){
            logger.error("error while connect with notification service",e);
            throw new RuntimeException("error when connect");
        }



    }





}
