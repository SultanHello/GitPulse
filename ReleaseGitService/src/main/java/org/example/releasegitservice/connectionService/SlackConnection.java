package org.example.releasegitservice.connectionService;


import lombok.AllArgsConstructor;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.services.CalligraphyService;
import org.example.releasegitservice.services.ReleaseService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class SlackConnection {


    private final RestTemplate restTemplate;
    private final CalligraphyService calligraphyService;
    public void sendMessage(Release release, Starter starter){
        String string=calligraphyService.view(release);
        System.out.println(1);
        restTemplate.postForObject(
                "http://NOTIFICATIONSENDERGITSERVICE/notification/sendSlack?message="+string,
                starter,
                Void.class
        );
        System.out.println(2);


    }




}
