package org.example.releasegitservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.repositories.ReleaseRepository;
import org.example.releasegitservice.services.GitHubService;
import org.example.releasegitservice.services.ReleaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ReleaseService releaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGetGitUsername_success() {
        logger.info("Starting test for getGitUsername method");
        String authHeader = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhc2ltYmVrMDZAbWFpbC5ydSIsImlhdCI6MTcyNzkzNDQ5OCwiZXhwIjoxNzI4MDIwODk4fQ.UEbO8eL9oLV4OETB5WF686Jrc7dZTzDzus8vQCSGJr9uTnIiK2C4NVyzpOxmpj3T"; // пример заголовка авторизации
        String expectedUsername = "testUser";


        logger.info("Setting up mock");
        when(releaseService.getGitUsername(authHeader)).thenReturn(expectedUsername);
        String username= releaseService.getGitUsername(authHeader);

        logger.info("Checking the results");
        assertEquals(expectedUsername, username);


    }
    @Test
    void testGetGitUsername_null() {
        logger.info("Starting test for getGitUsername method");

        String url = "http://USERGITSERVICE/users/getGitUsername?token=test_token";

        logger.info("Setting up mock");
        when(restTemplate.getForObject(url, String.class)).thenReturn(null);
        String username = releaseService.getGitUsername(anyString());



        logger.info("checking");
        assertEquals(restTemplate.getForObject(url, String.class),username);



    }
    @Test
    void testAddReleases() {

        logger.info("access for test");
        String authHeader = "testAuthHeader";
        Starter starter = new Starter();
        starter.setToken("testUrl");
        starter.setRepoName("testRepo");


        String mockGitUsername = "testUser";

        logger.info("setting up for mock in method getGitUsername");
        when(releaseService.getGitUsername(authHeader)).thenReturn(mockGitUsername);

        JsonNode[] mockReleases = new JsonNode[]{
                createMockRelease(1L,"ASD","URL","data")
        };
        logger.info("setting up for mock in method getReleasesFromGitHub");
        when(releaseService.getReleasesFromGitHub(anyString(), eq(starter), anyString()))
                .thenReturn(mockReleases);
        logger.info("setting up for mock in method saveReleases");

        when(releaseService.saveReleases(eq(mockReleases),anyString(),eq(starter))).thenReturn("releases saved to dataBase");

        logger.info("checking");
        releaseService.addReleases(starter, authHeader);
        assertEquals(releaseService.saveReleases(mockReleases, "someAuthHeader", starter),"releases saved to dataBase");




    }
    private JsonNode createMockRelease(long id, String name, String url, String date) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode releaseNode = mapper.createObjectNode();
        releaseNode.put("id", id);
        releaseNode.put("name", name);
        releaseNode.put("url", url);
        releaseNode.put("created_at", date); // или любое другое поле, которое вам нужно
        return releaseNode;
    }



}
