package roomescape.service.dto;

import roomescape.domain.Theme;

public class ThemeResponse {

    private final long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeResponse(Theme theme) {
        this.id = theme.getId();
        this.name = theme.getName();
        this.description = theme.getDescription();
        this.thumbnail = theme.getThumbnail();
    }

    public long getId() {
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
