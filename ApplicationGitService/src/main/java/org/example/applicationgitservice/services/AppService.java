package org.example.applicationgitservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import org.example.applicationgitservice.models.CopyRelease;
import org.example.applicationgitservice.models.Starter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class AppService {
    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(AppService.class);

    public void start(Starter starter, String authHeader){

        logger.info("starting work with http header");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        String starterJson;
        try {
            logger.info("starting convert object {} to JSON",starter.toString());
            starterJson = objectMapper.writeValueAsString(starter);
        } catch (Exception e) {
            logger.error("Error converting Starter object to JSON: {}",e.getMessage());
            throw  new RuntimeException("JSON conversion failed");
        }
        HttpEntity<String> entity = new HttpEntity<>(starterJson, headers);
        String url = "http://RELEASEGITSERVICE/releases/addRepo";
        logger.info("change JSON to object {}",starterJson);

        while (true) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                if(response.getStatusCode().equals(HttpStatus.OK)){
                    logger.info("success request OK : {}",response);
                    Thread.sleep(1000);
                }else{
                    logger.error("error connection thread was interrupt");
                    break;
                }

            } catch (Exception e) {
                Thread.currentThread().interrupt();
                logger.error("error connection thread was interrupt");
                break;
            }
        }
    }

}
