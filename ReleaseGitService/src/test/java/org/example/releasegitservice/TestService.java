package org.example.releasegitservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.aspectj.lang.annotation.Before;
import org.example.releasegitservice.connectionService.EmailConnection;
import org.example.releasegitservice.connectionService.SlackConnection;
import org.example.releasegitservice.integratedTest.TestServiceIntegrated;
import org.example.releasegitservice.models.Release;
import org.example.releasegitservice.models.Starter;
import org.example.releasegitservice.repositories.ReleaseRepository;
import org.example.releasegitservice.services.GitHubService;
import org.example.releasegitservice.services.ReleaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestService {

    @Mock
    private ReleaseRepository repository;

    @Mock
    private Starter starter;
    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private ReleaseService releaseService;
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);
    @Mock
    private Logger logger1;


    public TestService() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReleases() {
        when(starter.getRepoName()).thenReturn("testRepo");

        logger.info("creating data for jsonNode");
        JsonNode mockRelease1 = mock(JsonNode.class);
        JsonNode mockRelease2 = mock(JsonNode.class);


        logger.info("mocking return something for first release");
        JsonNode mockBody1 = mock(JsonNode.class);
        JsonNode mockPublishedAt1 = mock(JsonNode.class);
        JsonNode mockTagName1 = mock(JsonNode.class);
        JsonNode mockId1 = mock(JsonNode.class);
        JsonNode mockName1 = mock(JsonNode.class);
        JsonNode mockHtmlUrl1 = mock(JsonNode.class);

        when(mockRelease1.get("body")).thenReturn(mockBody1);
        when(mockBody1.asText()).thenReturn("Test body 1");
        when(mockRelease1.get("published_at")).thenReturn(mockPublishedAt1);
        when(mockPublishedAt1.asText()).thenReturn("2023-10-01T12:00:00Z");
        when(mockRelease1.get("tag_name")).thenReturn(mockTagName1);
        when(mockTagName1.asText()).thenReturn("v1.0");
        when(mockRelease1.get("id")).thenReturn(mockId1);
        when(mockId1.asLong()).thenReturn(1L);
        when(mockRelease1.get("name")).thenReturn(mockName1);
        when(mockName1.asText()).thenReturn("Release 1");
        when(mockRelease1.get("html_url")).thenReturn(mockHtmlUrl1);
        when(mockHtmlUrl1.asText()).thenReturn("http://release1.com");


        logger.info("mocking return something for second release");
        JsonNode mockBody2 = mock(JsonNode.class);
        JsonNode mockPublishedAt2 = mock(JsonNode.class);
        JsonNode mockTagName2 = mock(JsonNode.class);
        JsonNode mockId2 = mock(JsonNode.class);
        JsonNode mockName2 = mock(JsonNode.class);
        JsonNode mockHtmlUrl2 = mock(JsonNode.class);

        when(mockRelease2.get("body")).thenReturn(mockBody2);
        when(mockBody2.asText()).thenReturn("Test body 2");
        when(mockRelease2.get("published_at")).thenReturn(mockPublishedAt2);
        when(mockPublishedAt2.asText()).thenReturn("2023-10-02T12:00:00Z");
        when(mockRelease2.get("tag_name")).thenReturn(mockTagName2);
        when(mockTagName2.asText()).thenReturn("v2.0");
        when(mockRelease2.get("id")).thenReturn(mockId2);
        when(mockId2.asLong()).thenReturn(2L);
        when(mockRelease2.get("name")).thenReturn(mockName2);
        when(mockName2.asText()).thenReturn("Release 2");
        when(mockRelease2.get("html_url")).thenReturn(mockHtmlUrl2);
        when(mockHtmlUrl2.asText()).thenReturn("http://release2.com");

        JsonNode[] releases = {mockRelease1, mockRelease2};


        logger.info("starting does method");

        releaseService.saveReleases(releases, "testUser", starter);
        logger.info("Capture the arguments that are passed to the save method");

        ArgumentCaptor<Release> releaseCaptor = ArgumentCaptor.forClass(Release.class);
        verify(repository, times(2)).save(releaseCaptor.capture());


        logger.info("Checking the first saved release");
        Release capturedRelease1 = releaseCaptor.getAllValues().get(0);
        assertEquals("Test body 1", capturedRelease1.getDescription());
        assertEquals("testUser", capturedRelease1.getGitUsername());
        assertEquals("testRepo", capturedRelease1.getRepositoryName());
        assertEquals("v1.0", capturedRelease1.getTagName());
        assertEquals(1L, capturedRelease1.getId());
        assertEquals("Release 1", capturedRelease1.getReleaseName());
        assertEquals("http://release1.com", capturedRelease1.getReleaseUrl());
        assertEquals(OffsetDateTime.parse("2023-10-01T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .atZoneSameInstant(ZoneOffset.ofHours(5)).toOffsetDateTime().toLocalDateTime(),
                capturedRelease1.getReleaseDate());
        logger.info("Checking the second saved release");

        Release capturedRelease2 = releaseCaptor.getAllValues().get(1);
        assertEquals("Test body 2", capturedRelease2.getDescription());
        assertEquals("testUser", capturedRelease2.getGitUsername());
        assertEquals("testRepo", capturedRelease2.getRepositoryName());
        assertEquals("v2.0", capturedRelease2.getTagName());
        assertEquals(2L, capturedRelease2.getId());
        assertEquals("Release 2", capturedRelease2.getReleaseName());
        assertEquals("http://release2.com", capturedRelease2.getReleaseUrl());
        assertEquals(OffsetDateTime.parse("2023-10-02T12:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        .atZoneSameInstant(ZoneOffset.ofHours(5)).toOffsetDateTime().toLocalDateTime(),
                capturedRelease2.getReleaseDate());
    }


    @Test
    void testGetReleasesFromGitHub_success() {
        when(starter.getRepoName()).thenReturn("testRepo");
        JsonNode release1 = mock(JsonNode.class);
        JsonNode release2 = mock(JsonNode.class);

        JsonNode publishedAt1 = mock(JsonNode.class);
        JsonNode publishedAt2 = mock(JsonNode.class);

        when(release1.get("published_at")).thenReturn(publishedAt1);
        when(publishedAt1.asText()).thenReturn("2023-10-01T12:00:00Z");

        when(release2.get("published_at")).thenReturn(publishedAt2);
        when(publishedAt2.asText()).thenReturn("2023-10-02T12:00:00Z");

        JsonNode[] releases = {release1, release2};
        when(gitHubService.getReleases("testUser", "testRepo", "testUrl")).thenReturn(releases);

        // Выполнение метода
        JsonNode[] result = releaseService.getReleasesFromGitHub("testUser", starter, "testUrl");

        // Проверка результатов
        assertNotNull(result);
        assertEquals(2, result.length);
        assertSame(release2, result[0]); // проверка сортировки: второй релиз должен быть первым
        assertSame(release1, result[1]);


    }
}
