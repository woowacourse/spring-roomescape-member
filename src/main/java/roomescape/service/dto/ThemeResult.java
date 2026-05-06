package roomescape.service.dto;

import roomescape.domain.Theme;

public class ThemeResult {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeResult(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static ThemeResult from(Theme theme) {
        return new ThemeResult(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
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

    public String getThumbnail() {
        return thumbnail;
    }
}
