package org.example.emailsendergitservice.services;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.example.emailsendergitservice.model.Starter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SlackSenderService {


    public void sendNotification(String message, Starter starter){
        String WEBHOOK_URL = starter.getWebhoockUrl();
        System.out.println(WEBHOOK_URL);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(WEBHOOK_URL);
            post.setHeader("Content-Type", "application/json");
            String json = "{\"text\":\"" + message + "\"}";
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            HttpResponse response = client.execute(post);
            if (response == null) {
                System.out.println("No response received.");

            }

            int statusCode = response.getCode();
            HttpEntity entity = ((CloseableHttpResponse) response).getEntity();
            String responseBody = EntityUtils.toString(entity);

            System.out.println("Status: " + statusCode);
            System.out.println("Response: " + responseBody);




        } catch (Exception e) {
            System.out.println("An error occurred while sending the notification.");
            e.printStackTrace();
        }
    }
}
