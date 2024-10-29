package org.example.releasegitservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data


public class Starter {
    private String webhoockUrl;
    private String repoName;
    private String token;
    private String gitHubUrl;
    private String message;
}

