package org.example.roomgitservice.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatingGroup {
    private String groupName;
    private  Developer user;

}
