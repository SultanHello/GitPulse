package org.example.releasegitservice.models;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "release_info")
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
