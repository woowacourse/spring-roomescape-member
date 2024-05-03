package roomescape.controller.response;

import roomescape.model.Theme;

public class ThemeResponse {

    private final long themeId;
    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeResponse(long themeId, String name, String description, String thumbnail) {
        this.themeId = themeId;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public long getThemeId() {
        return themeId;
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
