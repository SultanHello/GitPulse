package org.example.applicationgitservice.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CopyRelease {
    private Long id;
    private String gitUsername;
    private String repositoryName;
    private String tagName;
    private String releaseName;
    private String description;
    private LocalDateTime releaseDate;
    private String releaseUrl;
}
