package org.example.releasegitservice.integratedTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.releasegitservice.connectionService.EmailConnection;
import org.example.releasegitservice.connectionService.SlackConnection;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.repositories.ReleaseRepository;
import org.example.releasegitservice.services.GitHubService;
import org.example.releasegitservice.services.ReleaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureMockMvc

public class TestServiceIntegrated {
    @Autowired
    private ReleaseService releaseService;
    private static final Logger logger = LoggerFactory.getLogger(TestServiceIntegrated.class);



    @Autowired
    private ReleaseRepository releaseRepository;

    @MockBean
    private SlackConnection slackConnection;

    @MockBean
    private EmailConnection emailConnection;




    @Test
    public void testNotifyIfNewReleases() {

        Starter starter = new Starter();
        String authHeader = "Bearer token";


        logger.info("adding new release to db");
        Release newRelease = Release.builder()
                .id(1L)
                .releaseDate(LocalDateTime.now().minusSeconds(5))
                .build();
        releaseRepository.save(newRelease);


        logger.info("call method");
        releaseService.notifyIfNewReleases(starter, authHeader);


        logger.info("get lest release");
        Release lastRelease = releaseRepository.findAll().get(releaseRepository.findAll().size() - 1);


        logger.info("check notification is sent");
        verify(slackConnection, times(1)).sendMessage(lastRelease, starter);
        verify(emailConnection, times(1)).sendMessage(lastRelease, starter, authHeader);
    }

    @Test
    public void testNotifyIfNoNewReleases() {
        Starter starter = new Starter();
        String authHeader = "Bearer token";


        logger.info("adding old release");
        Release oldRelease = Release.builder()
                .id(1L)
                .releaseDate(LocalDateTime.now().minusMinutes(1))
                .build();
        releaseRepository.save(oldRelease);


        logger.info("call method");
        releaseService.notifyIfNewReleases(starter, authHeader);


        logger.info("check notification is sent");
        verify(slackConnection, never()).sendMessage(any(), any());
        verify(emailConnection, never()).sendMessage(any(), any(), any());
    }





}
