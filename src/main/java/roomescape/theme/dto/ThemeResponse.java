package roomescape.theme.dto;

import roomescape.theme.model.Theme;

import java.time.LocalTime;

public class ThemeResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final LocalTime requiredTime;

    private ThemeResponse(Long id, String name, String description, String imageUrl, LocalTime requiredTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.requiredTime = requiredTime;
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getImageUrl(),
                theme.getRequiredTime()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalTime getRequiredTime() {
        return requiredTime;
    }
}
