package org.example.emailsendergitservice.services;

import org.example.emailsendergitservice.model.Starter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RestTemplate restTemplate;

    private Starter starter;

    @BeforeEach
    public void setUp() {
        starter = new Starter();
        starter.setToken("test-token");
    }

    @Test
    public void testSendEmail() {
        // Arrange
        String expectedEmail = "recipient@example.com";
        String emailText = "This is a test email";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + starter.getToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(
                "http://USERGITSERVICE/users/getEmail",
                HttpMethod.GET,
                entity,
                String.class
        )).thenReturn(ResponseEntity.ok(expectedEmail));

        // Act
        emailSenderService.sendEmail(starter, emailText);

        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("asimbek06@mail.ru", sentMessage.getFrom());
        assertEquals(expectedEmail, sentMessage.getTo()[0]);
        assertEquals("GitPulse", sentMessage.getSubject());
        assertEquals(emailText, sentMessage.getText());
    }
}
