package roomescape.core.dto;

public class ThemeRequestDto {

    private String name;
    private String description;
    private String thumbnail;

    public ThemeRequestDto() {
    }

    public ThemeRequestDto(final String name, final String description, final String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
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
