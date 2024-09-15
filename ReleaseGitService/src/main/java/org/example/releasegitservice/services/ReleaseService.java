package org.example.releasegitservice.services;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.example.releasegitservice.connectionService.SlackConnection;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.repositories.ReleaseRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class ReleaseService {
    private final ReleaseRepository repository;
    private final RestTemplate restTemplate;
    private final SlackConnection emailConnection;

    public List<Release> allReleases(){
        return repository.findAll();
    }



    public void addReleases(Starter starter, String authHeader){
        System.out.println(authHeader);

        String gitUsername=restTemplate.getForObject(
                "http://USERGITSERVICE/users/getUser?token="+authHeader.substring(7),
                String.class
        );
        System.out.println(4);
        String accessToken = "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";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String githubApiUrl = "https://api.github.com/repos/"+gitUsername+"/"+starter.getRepoName()+"/"+"/releases";

        RestTemplate restTemplate = new RestTemplate();
        System.out.println(5);
        ResponseEntity<JsonNode[]> response = restTemplate.exchange(githubApiUrl, HttpMethod.GET,
                entity, JsonNode[].class);

        System.out.println(6);
        JsonNode[] releases=response.getBody();
        List<Release> test = repository.findAll();
        if(test.size()==releases.length){
            System.out.println("error");
            return;
        }

        if (releases != null) {
            for (JsonNode release : releases) {
                repository.save(Release.builder()
                        .description(release.get("body").asText())
                        .gitUsername(gitUsername)
                        .releaseDate(OffsetDateTime.parse(release.get("published_at").asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                                .atZoneSameInstant(ZoneOffset.ofHours(5)).toOffsetDateTime().toLocalDateTime()
                        )
                        .repositoryName(starter.getRepoName())
                        .tagName(release.get("tag_name").asText())
                        .id(release.get("id").asLong())
                        .releaseName(release.get("name").asText())
                        .releaseUrl(release.get("html_url").asText())
                        .build());

            }
        }
        if(repository.findAll().get(repository.findAll().size()-1).getReleaseDate().isAfter(LocalDateTime.now().minusSeconds(10))){
            System.out.println(repository.findAll().get(repository.findAll().size()-1).toString());
            emailConnection.sendMessage(repository.findAll().get(repository.findAll().size()-1).toString(),starter);
        }






    }



}
