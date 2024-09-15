package org.example.applicationgitservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.applicationgitservice.models.CopyRelease;
import org.example.applicationgitservice.models.Starter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class AppService {
    private final RestTemplate restTemplate;
    public String start(Starter starter, String authHeader){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        String starterJson;
        try {
            starterJson = objectMapper.writeValueAsString(starter);
        } catch (Exception e) {
            System.err.println("Ошибка при преобразовании объекта Starter в JSON: " + e.getMessage());
            return "JSON conversion failed";
        }
        HttpEntity<String> entity = new HttpEntity<>(starterJson, headers);
        String url = "http://RELEASEGITSERVICE/releases/addRepo?repoName=" +starter.getRepoName();
        System.out.println("starter json ;       @@@@    : "+starterJson);

        while (true) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                System.out.println(response);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Поток был прерван");
            }
        }






    }

}
