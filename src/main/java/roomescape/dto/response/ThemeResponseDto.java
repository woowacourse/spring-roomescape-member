package roomescape.dto.response;

import roomescape.domain.Theme;

public class ThemeResponseDto {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    public ThemeResponseDto() {
    }

    public ThemeResponseDto(final Theme theme) {
        this(theme.getId(), theme);
    }

    public ThemeResponseDto(final Long id, final Theme theme) {
        this.id = id;
        this.name = theme.getName();
        this.description = theme.getDescription();
        this.thumbnail = theme.getThumbnail();
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
