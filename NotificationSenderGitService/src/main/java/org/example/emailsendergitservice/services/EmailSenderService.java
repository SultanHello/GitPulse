//package org.example.emailsendergitservice.services;
//import com.sendgrid.helpers.mail.objects.Email;
//
//import org.apache.hc.client5.http.classic.methods.HttpPost;
//import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.core5.http.HttpEntity;
//import org.apache.hc.core5.http.io.entity.EntityUtils;
//import org.apache.hc.core5.http.message.BasicNameValuePair;
//import org.springframework.stereotype.Service;
//
//
//import org.example.emailsendergitservice.model.SenderEmail;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class EmailSenderService {
//    private static final String MAILGUN_DOMAIN = "mg.asimbek.com";
//    private static final String MAILGUN_API_KEY = "d003d932ae3f3e97ce0127147ead73ae-826eddfb-dabb353f";   // Ваш API-ключ
//
//
//    public void sendEmail(SenderEmail sender) throws Exception {
//        String url = "https://api.mailgun.net/v3/" + MAILGUN_DOMAIN + "/messages";
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpPost post = new HttpPost(url);
//            post.setHeader("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(("api:" + MAILGUN_API_KEY).getBytes()));
//
//            // Формируем список параметров
//            List<BasicNameValuePair> params = new ArrayList<>();
//            params.add(new BasicNameValuePair("from", sender.getFromAddress()));
//            params.add(new BasicNameValuePair("to", sender.getToAddress()));
//            params.add(new BasicNameValuePair("subject", sender.getSubject()));
//            params.add(new BasicNameValuePair("text", sender.getText()));
//
//            // Устанавливаем параметры как form data
//            post.setEntity(new UrlEncodedFormEntity(params));
//
//            // Выполняем запрос
//            try (CloseableHttpResponse response = client.execute(post)) {
//                // Получаем статус напрямую через response.getCode() (альтернативный метод)
//                int statusCode = response.getCode();
//                System.out.println("Status Code: " + statusCode);
//
//                // Получаем тело ответа
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    String responseBody = EntityUtils.toString(entity);
//                    System.out.println("Response: " + responseBody);
//                } else {
//                    System.out.println("No response body received.");
//                }
//            }
//        }
//    }
//    private Email setEmail(String name, String emailAddress) {
//        Email email = new Email();
//        email.setEmail(emailAddress);
//        email.setName(name);
//        return email;
//    }
//
//
//}
