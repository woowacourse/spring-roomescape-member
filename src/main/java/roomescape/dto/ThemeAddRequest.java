package roomescape.dto;

import roomescape.domain.Theme;

public class ThemeAddRequest {
    private String name;
    private String description;
    private String thumbnail;

    ThemeAddRequest() {

    }

    public ThemeAddRequest(String name, String description, String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }


    public Theme toEntity() {
        return new Theme(null, name, description, thumbnail);
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
