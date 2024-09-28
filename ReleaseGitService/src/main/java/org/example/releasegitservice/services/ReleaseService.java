package org.example.releasegitservice.services;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.example.releasegitservice.connectionService.EmailConnection;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ReleaseService {
    private final ReleaseRepository repository;
    private final RestTemplate restTemplate;
    private final SlackConnection slackConnection;
    private final EmailConnection emailConnection;



    public List<Release> allReleases(){
        return repository.findAll();
    }



    public void addReleases(Starter starter, String authHeader){
        System.out.println(authHeader);
        String gitUsername =getGitUsername(authHeader);
        JsonNode[] releases = getReleasesFromGitHub(gitUsername,starter);

        List<Release> test = repository.findAll();
        if (releases.length == 0) {
            System.out.println("no repo releases");
            return;
        }

        if(!test.isEmpty()){
            if(test.get(test.size()-1).getId()==releases[0].get("id").asLong()){
                System.out.println("You haven't new releases");
                return;
            }

        }
        saveReleases(releases,gitUsername,starter);
        notifyIfNewReleases(starter,authHeader);

    }

    public String getGitUsername(String authHeader){
        return restTemplate.getForObject(
                "http://USERGITSERVICE/users/getGitUsername?token="+authHeader.substring(7),
                String.class
        );

    }
    public JsonNode[] getReleasesFromGitHub(String gitUsername,Starter starter){
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
        JsonNode[] releases = response.getBody();

        if (releases != null) {
            Arrays.sort(releases, (a, b) -> {
                LocalDateTime dateA = OffsetDateTime.parse(a.get("published_at").asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .atZoneSameInstant(ZoneOffset.ofHours(5)).toLocalDateTime();
                LocalDateTime dateB = OffsetDateTime.parse(b.get("published_at").asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .atZoneSameInstant(ZoneOffset.ofHours(5)).toLocalDateTime();
                return dateB.compareTo(dateA); // сортировка от новых к старым
            });
        }
        return releases;

    }

    private void saveReleases(JsonNode[] releases ,String gitUsername,Starter starter){
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

    }
    private void notifyIfNewReleases(Starter starter,String authHeader){
        if(repository.findAll().get(repository.findAll().size()-1).getReleaseDate().isAfter(LocalDateTime.now().minusSeconds(10))){
            System.out.println(repository.findAll().get(repository.findAll().size()-1).toString());
            slackConnection.sendMessage(repository.findAll().get(repository.findAll().size()-1),starter);
            emailConnection.sendMessage(repository.findAll().get(repository.findAll().size()-1),starter,authHeader);
        }

    }

    public List<String> getReposNames(String authHeader){
        System.out.println(1);
        String gitUsername = restTemplate.getForObject(
                "http://USERGITSERVICE/users/getGitUsername?token="+authHeader.substring(7),
                String.class
        );
        List<String> reposNames = new ArrayList<>();
        List<Release> releases = repository.findAll();
        System.out.println(releases);
        for(int i = 0;i<releases.size();i++){
            if(releases.get(i).getGitUsername().equals(gitUsername)){
                System.out.println(releases.get(i).getRepositoryName());

                if (!reposNames.contains(releases.get(i).getRepositoryName())){
                    reposNames.add(releases.get(i).getRepositoryName());
                }

            }

        }
        return reposNames;
    }
    public List<Release> getReposReleases(String authHeader ,String reposName){
        String gitUsername = restTemplate.getForObject(
                "http://USERGITSERVICE/users/getGitUsername?token="+authHeader.substring(7),
                String.class
        );
        List<Release> reposReleases =new ArrayList<>();
        List<Release> releases = repository.findAll();

        for (Release release:releases){

            if(release.getGitUsername().equals(gitUsername)){
                System.out.println(release.getRepositoryName()+" : "+reposName);
                if(release.getRepositoryName().equals(reposName)){
                    reposReleases.add(release);
                }

            }
        }
        return reposReleases;
    }




}
