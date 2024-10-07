package org.example.releasegitservice.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.releasegitservice.connectionService.EmailConnection;
import org.example.releasegitservice.connectionService.SlackConnection;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.repositories.ReleaseRepository;
import org.hibernate.annotations.ValueGenerationType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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

import org.slf4j.LoggerFactory;


@Service
@AllArgsConstructor

public class ReleaseService {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseService.class);
    private final GitHubService gitHubService;

    private final ReleaseRepository repository;
    private final RestTemplate restTemplate;
    private final SlackConnection slackConnection;
    private final EmailConnection emailConnection;




    public List<Release> allReleases(){
        return repository.findAll();
    }



    public void addReleases(Starter starter, String authHeader) {
        logger.info("starting work with auth header : {}",authHeader);
        try {
            String gitUsername =getGitUsername(authHeader);

            logger.info("gitUsername getted : {}",gitUsername);
            JsonNode[] releases = getReleasesFromGitHub(gitUsername,starter,starter.getGitHubUrl());
            logger.info("releases getted : {}",Arrays.toString(releases));
            List<Release> test=new ArrayList<>();
            for(int i = 0;i<repository.findAll().size();i++){
                if(repository.findAll().get(i).getRepositoryName().equals(starter.getRepoName())){
                    test.add(repository.findAll().get(i));
                }
            }

            if (releases.length == 0) {
                logger.info("no repo releases for repo : {}",starter.getRepoName());
                System.out.println("no repo releases");
                return;
            }



            if(!test.isEmpty()){
                if(test.get(test.size()-1).getId()==releases[0].get("id").asLong()){
                    logger.info("You haven't new releases because of repo {} with no new releases",starter.getRepoName());
                    return;
                }
            }

            logger.info("saving new releases for repo : {}",starter.getRepoName());

            saveReleases(releases,gitUsername,starter);
            logger.info("send new notification for repo : {}",starter.getRepoName());
            notifyIfNewReleases(starter,authHeader);

        }catch (Exception e) {
            logger.error("Error occurred in addReleases for repo: {} with authHeader: {}", starter.getRepoName(), authHeader, e);
            throw new RuntimeException("Error while adding releases", e);
        }
    }

    public String getGitUsername(String authHeader){
        String url = "http://USERGITSERVICE/users/getGitUsername?token="+authHeader.substring(7);
        try {
            logger.info("get git username by from url : {}",url);
            String gitUsername = restTemplate.getForObject(url,String.class);
            if (gitUsername == null) {
                logger.error("Git username is null");
                throw new RuntimeException("Git username cannot be null");
            }else{
                logger.info("result restTemplete for get git username : {}",gitUsername);
                return gitUsername;

            }



        }catch (Exception e){
            logger.error("error with :{}",url,e);
            throw  new RuntimeException("error while connect with userGitService");

        }

    }
    public JsonNode[] getReleasesFromGitHub(String gitUsername,Starter starter,String gitHubUrl) {
        logger.info("getting information from repo : {}",starter.getRepoName());
        JsonNode[] releases = gitHubService.getReleases(gitUsername, starter.getRepoName(),gitHubUrl);
        logger.info("received releases from repo : {} ,getted releases {}",starter.getRepoName(),Arrays.toString(releases));
        logger.info("starting if {} null or not null",Arrays.toString(releases));
        if (releases == null) {
            logger.error("error: releases not found from repo {}", starter.getRepoName());
            return null;
        }
        logger.info("releases {} is getted",Arrays.toString(releases));
        try{
            logger.info("starting sorting releases {} releases for program performance from repo : {}",Arrays.toString(releases),starter.getRepoName());
            Arrays.sort(releases, (a, b) -> {
                LocalDateTime dateA = OffsetDateTime.parse(a.get("published_at").asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .atZoneSameInstant(ZoneOffset.ofHours(5)).toLocalDateTime();
                LocalDateTime dateB = OffsetDateTime.parse(b.get("published_at").asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .atZoneSameInstant(ZoneOffset.ofHours(5)).toLocalDateTime();
                return dateB.compareTo(dateA);
            });
            logger.info("releases sorted successfully");

        }catch (Exception e){
            logger.error("error while sorting releases : {}",Arrays.toString(releases),e);
            throw new RuntimeException("error with sorting");
        }
        return releases;


    }

    public String saveReleases(JsonNode[] releases ,String gitUsername,Starter starter){
        try {
            logger.info("starting saving all releases from repo : {}",starter.getRepoName());
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

            logger.info("releases saved to dataBase");
            return "releases saved to dataBase";
        }catch (Exception e){
            logger.error("error while saving releases ",e);
            throw new RuntimeException("error while saving releases ");
        }
    }
    public void notifyIfNewReleases(Starter starter,String authHeader){
        List<Release> releases = repository.findAll();
        Release lastRelease = releases.get(releases.size() - 1);

        try {
            logger.info("sending release notification whose : {}",lastRelease);
            logger.info("sending release {} to slack connection class",lastRelease);
            emailConnection.sendMessage(lastRelease,starter,authHeader);
            logger.info("success sent");
            slackConnection.sendMessage(lastRelease,starter);
            logger.info("sending releases {} to email connection class",lastRelease);
        }catch (Exception e){
            logger.error("error while sending notification for release: {}",lastRelease,e);
            throw  new RuntimeException("problem with sending notification",e);
        }


    }

    public List<String> getReposNames(String authHeader){

        logger.info("starting connect with service for get git username");
        String gitUsername;
        try {
            gitUsername = getGitUsername(authHeader);
            logger.info("received git username: {}", gitUsername);
        } catch (Exception e) {
            logger.error("error while retrieving git username", e);
            throw new RuntimeException("error fetching git username", e);
        }

        try{
            List<String> reposNames = new ArrayList<>();
            logger.info("getting all releases");
            List<Release> releases = repository.findAll();
            for(int i = 0;i<releases.size();i++){
                if(releases.get(i).getGitUsername().equals(gitUsername)){
                    if (!reposNames.contains(releases.get(i).getRepositoryName())){
                        reposNames.add(releases.get(i).getRepositoryName());
                    }
                }
            }
            logger.info("received releases from gitUsername : {}",gitUsername);
            return reposNames;

        }catch (Exception e){
            logger.error("Problem while find  git username {} releases",gitUsername,e);
            throw new RuntimeException("error with find gitUsername releases");
        }


    }

    public List<Release> getReposReleases(String authHeader ,String reposName){
        logger.info("starting connect with service for get git username");
        String gitUsername;
        try {
            gitUsername = getGitUsername(authHeader);
            logger.info("received git username: {}", gitUsername);
        } catch (Exception e) {
            logger.error("error while retrieving git username", e);
            throw new RuntimeException("error fetching git username", e);
        }
        try {
            List<Release> reposReleases =new ArrayList<>();
            logger.info("getting all releases");

            List<Release> releases = repository.findAll();

            for (Release release:releases){
                if(release.getGitUsername().equals(gitUsername)){
                    if(release.getRepositoryName().equals(reposName)){
                        reposReleases.add(release);
                    }

                }
            }
            logger.info("received releases from gitUsername : {}",gitUsername);
            return reposReleases;

        }catch (Exception e){
            logger.error("Problem while find  git username {} releases",gitUsername,e);
            throw new RuntimeException("error with find gitUsername releases");
        }
    }


}
