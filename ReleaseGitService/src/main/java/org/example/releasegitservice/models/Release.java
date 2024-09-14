package org.example.releasegitservice.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "release_info")
public class Release {
    @Id

    private Long id;
    private String gitUsername;
    private String repositoryName;
    private String tagName;
    private String releaseName;
    private String description;
    private LocalDateTime releaseDate;
    private String releaseUrl;
}
