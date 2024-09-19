package org.example.releasegitservice.services;

import org.example.releasegitservice.models.Release;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class CalligraphyService {
    public String view(Release release) {
        return String.format(
                "✨ Release Information ✨\n" +
                        "-----------------------------------\n" +
                        "📦 Release Name: %s\n" +
                        "👤 GitHub Username: %s\n" +
                        "📁 Repository Name: %s\n" +
                        "🏷️ Tag: %s\n" +
                        "📜 Description: %s\n" +
                        "📅 Release Date: %s\n" +
                        "🔗 Release URL: %s\n" +
                        "-----------------------------------\n" +
                        "Thank you for checking out this release!"
                ,
                release.getReleaseName(),
                release.getGitUsername(),
                release.getRepositoryName(),
                release.getTagName(),
                release.getDescription(),
                release.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                release.getReleaseUrl()
        );

    }
}
