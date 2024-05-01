package roomescape.controller.request;

import java.util.Objects;
import roomescape.domain.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {
    public ThemeRequest {
        Objects.requireNonNull(name);
        Objects.requireNonNull(description);
        Objects.requireNonNull(thumbnail);
        validateName(name);
        validateDescription(description);
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("테마의 이름에 빈 값이 들어갈 수 없습니다.");
        }
    }

    private void validateDescription(String description) {
        if (description.isBlank()) {
            throw new IllegalArgumentException("테마의 설명에 빈 값이 들어갈 수 없습니다.");
        }
    }

    public Theme toEntity() {
        return new Theme(name, description, thumbnail);
    }
}
