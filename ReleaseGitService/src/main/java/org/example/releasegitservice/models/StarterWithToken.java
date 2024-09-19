package org.example.releasegitservice.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StarterWithToken {
    private String webhoockUrl;
    private String repoName;
    private String token;
}
