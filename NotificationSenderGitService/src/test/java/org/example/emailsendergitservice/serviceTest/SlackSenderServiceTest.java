package org.example.emailsendergitservice.serviceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.emailsendergitservice.model.Starter;
import org.example.emailsendergitservice.services.SlackSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
@SpringBootTest
public class SlackSenderServiceTest {



    @InjectMocks
    private SlackSenderService slackSenderService;



    @Test
    public void testSendNotificationSucces() {

        String message = "only test";
        Starter starter = new Starter();
        starter.setWebhoockUrl("https://hooks.slack.com/services/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"); // замените на ваш реальный webhook

        String sender = slackSenderService.sendNotification(message, starter);

        assertEquals(sender,"success send slack");

    }
    @Test
    public void testSendNotificationError() {

        String message = "only test for error";
        Starter starter = new Starter();
        starter.setWebhoockUrl("null");


        try {
            slackSenderService.sendNotification(message, starter);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("An error occurred while sending the notification", e.getMessage());
        }



    }



}
