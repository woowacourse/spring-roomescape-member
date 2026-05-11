package roomescape.controller.dto;

import roomescape.domain.Theme;

public class ThemeResponse {
    private final long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;

    private ThemeResponse(long id, String name, String description, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static ThemeResponse toDto(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}
