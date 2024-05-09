package roomescape.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.Theme;

public record ThemeSaveRequest(
        @NotBlank(message = "테마 제목은 빈칸일 수 없습니다.") String name,
        @NotNull(message = "테마 설명은 널일 수 없습니다.") String description,
        @NotNull(message = "테마 썸네일 URL은 null일 수 없습니다.") String thumbnail) {

    public static Theme toEntity(ThemeSaveRequest request) {
        return new Theme(request.name(), request.description(), request.thumbnail());
    }
}
