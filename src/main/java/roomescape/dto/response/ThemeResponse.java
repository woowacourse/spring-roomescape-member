package roomescape.dto.response;

import roomescape.domain.Name;
import roomescape.domain.Theme;

public record ThemeResponse(Long id, String name, String description, String thumbnail) {

    public ThemeResponse {
        validate(id, name, description, thumbnail);
    }

    private void validate(Long id, String name, String description, String thumbnail) {
        if (id == null) {
            throw new IllegalArgumentException("잘못된 응답입니다. id = " + id);
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("잘못된 응답입니다. name = " + name);
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("잘못된 응답입니다. description = " + description);
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("잘못된 응답입니다. thumbnail = " + thumbnail);
        }
    }

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName().getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    public Theme toTheme() {
        return new Theme(id, new Name(name), description, thumbnail);
    }
}
