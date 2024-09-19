package org.example.releasegitservice.connectionService;

import lombok.AllArgsConstructor;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.models.StarterWithToken;
import org.example.releasegitservice.services.CalligraphyService;
import org.example.releasegitservice.services.ReleaseService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class EmailConnection {
    private final RestTemplate restTemplate;
    private final CalligraphyService calligraphyService;



    public void sendMessage(Release release, Starter starter, String authHeader){


        String string=calligraphyService.view(release);;
        System.out.println(1);
        StarterWithToken starterWithToken =StarterWithToken.builder()
                .repoName(starter.getRepoName())
                .webhoockUrl(starter.getWebhoockUrl())
                .token(authHeader.substring(7))
                .build();
        restTemplate.postForObject(
                "http://NOTIFICATIONSENDERGITSERVICE/notification/sendEmail?message="+string,
                starterWithToken,
                Void.class
        );
        System.out.println(2);


    }
}
