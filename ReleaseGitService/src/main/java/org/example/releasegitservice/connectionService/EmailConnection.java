package org.example.releasegitservice.connectionService;

import lombok.AllArgsConstructor;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.services.CalligraphyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class EmailConnection {
    private static final Logger logger = LoggerFactory.getLogger(EmailConnection.class);

    private final RestTemplate restTemplate;
    private final CalligraphyService calligraphyService;

    public void sendMessage(Release release, Starter starter, String authHeader){
        try {
            String string=calligraphyService.view(release);
            if (string == null) {
                logger.error("Generated message string is null for release: {}", release);
                return;
            }
            starter.setToken(authHeader.substring(7));
            logger.info("starting connecting with notification for send email message : {}",string);
            ResponseEntity<Void> response =restTemplate.postForEntity(
                    "http://NOTIFICATIONSENDERGITSERVICE/notification/sendEmail?message="+string,
                    starter,
                    Void.class
            );
            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Failed to send email notification, status code: {}", response.getStatusCode());
                throw new RuntimeException("Failed to send email notification");
            }
            logger.info("success sent email message : {}",string);

        }catch (Exception e){
            logger.error("error while connect with notification service",e);
            throw new RuntimeException("error when connect");
        }

    }


}
