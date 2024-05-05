package roomescape.service.dto.request;

import roomescape.domain.Theme;

public record ThemeSaveRequest(String name, String description, String thumbnail) {

    public ThemeSaveRequest {
        validateNameBlank(name);
    }

    private void validateNameBlank(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 빈칸일 수 없습니다.");
        }
    }

    public static Theme toEntity(ThemeSaveRequest request) {
        return new Theme(request.name(), request.description(), request.thumbnail());
    }
}
