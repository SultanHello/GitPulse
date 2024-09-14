package org.example.applicationgitservice.services;

import lombok.AllArgsConstructor;
import org.example.applicationgitservice.models.CopyRelease;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class AppService {
    private final RestTemplate restTemplate;
    public String start(String repoName,String authHeader){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);


        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://RELEASEGITSERVICE/releases/addRepo?repoName=" + repoName;
        System.out.println(2);
        while (true) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
                System.out.println(response);

                // Добавляем задержку на 10 секунд между запросами
                Thread.sleep(1000); // 10000 миллисекунд = 10 секунд
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Поток был прерван");
            }
        }






    }

}
