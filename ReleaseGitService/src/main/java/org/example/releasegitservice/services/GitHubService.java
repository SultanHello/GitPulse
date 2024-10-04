package org.example.releasegitservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@AllArgsConstructor
@Service
public class GitHubService {
    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);


    public JsonNode[] getReleases(String gitUsername, String repoName,String accessToken){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            logger.info("adding auth token {} for entity",accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String githubApiUrl = "https://api.github.com/repos/" + gitUsername + "/" + repoName + "/releases";
            RestTemplate restTemplate = new RestTemplate();
            logger.info("connecting with git hub api for repo {} and getting releases",repoName);

            ResponseEntity<JsonNode[]> response = restTemplate.exchange(githubApiUrl, HttpMethod.GET, entity, JsonNode[].class);
            logger.info("success receive info releases : {}", Arrays.toString(response.getBody()));
            if (response.getBody() == null) {
                logger.error("No releases found for repo: {}", repoName);
                return null;
            }
            return response.getBody();

        }catch (Exception e ){
            logger.error("error while connecting with git hub api",e);
            throw new RuntimeException("error with connecting");
        }


    }
}