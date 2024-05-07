package roomescape.dto;

import java.util.Objects;
import roomescape.domain.theme.Theme;

public record ThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public ThemeRequest {
        try {
            Objects.requireNonNull(name);
            Objects.requireNonNull(description);
            Objects.requireNonNull(thumbnail);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("null 값이 될 수 없습니다.");
        }
    }

    public Theme toEntity() {
        return new Theme(name, description, thumbnail);
    }
}
