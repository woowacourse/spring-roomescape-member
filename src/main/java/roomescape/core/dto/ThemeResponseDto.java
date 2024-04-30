package roomescape.core.dto;

import roomescape.core.domain.Theme;

public class ThemeResponseDto {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    public ThemeResponseDto() {
    }

    public ThemeResponseDto(final Long id, final Theme theme) {
        this(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public ThemeResponseDto(final Long id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
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
