package org.example.usergitservice.models;


import lombok.Data;

@Data
public class RegUser {
    private String email;
    private String password;
    private String gitUsername;
}
