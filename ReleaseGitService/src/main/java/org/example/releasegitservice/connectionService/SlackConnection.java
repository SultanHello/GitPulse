package org.example.releasegitservice.connectionService;


import lombok.AllArgsConstructor;
import org.example.releasegitservice.models.Starter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class SlackConnection {

    private final RestTemplate restTemplate;
    public String sendMessage(String string, Starter starter){
        System.out.println(1);
        restTemplate.postForObject(
                "http://NOTIFICATIONSENDERGITSERVICE/notification/sendSlack?message="+string,
                starter,
                Void.class
        );
        System.out.println(2);
        return "success";

    }




}
