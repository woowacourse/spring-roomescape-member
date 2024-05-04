package roomescape.domain;

import roomescape.exception.BadRequestException;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validateNotBlank(name, description, thumbnail);
        validateThumbnailFormat(thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private void validateNotBlank(String name, String description, String thumbnail) {
        if (name.isBlank() || description.isBlank() || thumbnail.isBlank()) {
            throw new BadRequestException("테마의 정보는 비어있을 수 없습니다.");
        }
    }

    private void validateThumbnailFormat(String thumbnail) {
        if (!thumbnail.matches("(http|https).*")) {
            throw new BadRequestException("썸네일은 url 링크여야합니다.");
        }
    }

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, Theme theme) {
        this(id, theme.name, theme.description, theme.thumbnail);
    }

    public boolean isDuplicated(Theme theme) {
        return name.equals(theme.name);
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
