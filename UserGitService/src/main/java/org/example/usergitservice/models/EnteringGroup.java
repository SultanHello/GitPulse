package org.example.usergitservice.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class EnteringGroup {
    private Long id;
    private User user;
}
