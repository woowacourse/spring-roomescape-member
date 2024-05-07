package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import roomescape.domain.Theme;

public record ThemeCreateRequest(
        Long id,
        @NotBlank(message = "[ERROR] 테마 이름은 비어있을 수 없습니다.")
        String name,
        @NotEmpty(message = "[ERROR] 테마 설명은 비어있을 수 없습니다.")
        String description,
        @NotEmpty(message = "[ERROR] 테마 썸네일은 비어있을 수 없습니다.")
        String thumbnail
) {

    public static Theme toTheme(final ThemeCreateRequest request) {
        return new Theme(request.id(), request.name(), request.description(), request.thumbnail());
    }
}
