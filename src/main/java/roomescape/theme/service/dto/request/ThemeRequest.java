package roomescape.theme.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.theme.entity.Theme;

public record ThemeRequest(
        @NotBlank(message = "테마 제목이 입력되지 않았습니다.")
        String name,
        @NotBlank(message = "설명이 입력되지 않았습니다.")
        String description,
        @NotBlank(message = "썸네일이 입력되지 않았습니다.")
        String thumbnail
) {
    public Theme toEntity() {
        return new Theme(null, name, description, thumbnail);
    }
}
