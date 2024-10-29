package org.example.emailsendergitservice.serviceTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


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


    }
    @Test
    public void testSendNotificationError() {



    }



}
