package roomescape.dto;

import java.util.Objects;
import roomescape.domain.theme.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public ThemeRequest {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    private void validateThumbnail(final String thumbnail) {
        if (Objects.isNull(thumbnail)) {
            throw new IllegalArgumentException("썸네일 값은 null 값이 될 수 없습니다.");
        }
    }

    private void validateDescription(final String description) {
        if (Objects.isNull(description)) {
            throw new IllegalArgumentException("설명은 null 값이 될 수 없습니다.");
        }
    }

    private void validateName(final String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 null 값이 될 수 없습니다.");
        }
    }

    public Theme toEntity() {
        return new Theme(name, description, thumbnail);
    }
}
