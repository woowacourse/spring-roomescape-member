package roomescape.dto.request;

import roomescape.common.exception.InvalidRequestException;
import roomescape.domain.Theme;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnail
) {
    public ThemeCreateRequest {
        validate(name);
    }

    public Theme toTheme() {
        return new Theme(null, name, description, thumbnail);
    }

    private void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }
    }
}
