package org.example.usergitservice.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CretingGroup {
    private String groupName;
    private User user;
}
