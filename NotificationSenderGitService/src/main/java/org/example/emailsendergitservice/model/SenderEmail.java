package org.example.emailsendergitservice.model;


import lombok.Data;

@Data
public class SenderEmail {
    private String toAddress;
    private String subject;
    private String text;
}
