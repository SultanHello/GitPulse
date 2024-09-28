package org.example.releasegitservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.hc.core5.http.HttpEntity;
import org.example.releasegitservice.connectionService.EmailConnection;
import org.example.releasegitservice.connectionService.SlackConnection;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.repositories.ReleaseRepository;
import org.example.releasegitservice.services.ReleaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.verification.VerificationMode;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static javax.management.Query.eq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class TestService {
    @Mock
    private ReleaseRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SlackConnection slackConnection;

    @Mock
    private EmailConnection emailConnection;
    @InjectMocks
    private ReleaseService releaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        releaseService  = new ReleaseService(repository,restTemplate,slackConnection,emailConnection); // Инициализация сервиса с моками
    }


    @Test
    public void testGetGitUsername() {

        String token = "Bearer ewclwemvwevmlwevmlwvwev";
        String expectedUsername = "testUser";



        when(restTemplate.getForObject(anyString(), ArgumentMatchers.eq(String.class))).thenReturn(expectedUsername);

        String actualUsername = releaseService.getGitUsername(token);

        assertEquals(expectedUsername, actualUsername);
        Mockito.verify(restTemplate, times(1)).getForObject(anyString(), ArgumentMatchers.eq(String.class));

    }
    @Test
    public void testGetReleasesFromGitHub(){
        String gitUsername="username";
        Starter starter = new Starter();
        starter.setRepoName("testRepo");
        JsonNode mockRelease = mock(JsonNode.class);
        when(mockRelease.get("id")).thenReturn(new LongNode(1));
        when(mockRelease.get("published_at")).thenReturn(new TextNode("2023-09-15T12:34:56Z"));
        when(mockRelease.get("body")).thenReturn(new TextNode("Release body"));
        when(mockRelease.get("tag_name")).thenReturn(new TextNode("v1.0.0"));
        when(mockRelease.get("name")).thenReturn(new TextNode("First Release"));
        when(mockRelease.get("html_url")).thenReturn(new TextNode("https://github.com/test/release"));
        JsonNode[] expectedJsonNode =new JsonNode[]{mockRelease};

//        when(restTemplate.exchange(
//                anyString(),
//                eq(HttpMethod.GET),
//                any(HttpEntity.class),
//                eq(JsonNode[].class)
//        ))
//                .thenReturn(new ResponseEntity<>(expectedJsonNode, HttpStatus.OK));

        JsonNode[] actualReleases = releaseService.getReleasesFromGitHub(gitUsername,starter);

        assertNotNull(actualReleases);
        assertEquals(expectedJsonNode[0].get("id"),actualReleases[0].get("id"));
        assertEquals(expectedJsonNode[0].get("tag_name"),actualReleases[0].get("tag_name"));


        Mockito.verify(restTemplate, times(1)).exchange(anyString(), (HttpMethod) ArgumentMatchers.eq(HttpMethod.GET), (org.springframework.http.HttpEntity<?>) any(HttpEntity.class), ArgumentMatchers.eq(JsonNode[].class));



    }




}
