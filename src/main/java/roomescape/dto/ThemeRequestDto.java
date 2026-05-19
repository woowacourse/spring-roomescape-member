package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Theme;

public record ThemeRequestDto(
        @NotBlank(message = "이름은 비어 있을 수 없습니다.")
        String name,

        @NotBlank(message = "설명은 비어 있을 수 없습니다.")
        String description,

        @NotBlank(message = "썸네일은 비어 있을 수 없습니다.")
        String thumbnailUrl
) {
    public Theme toEntity() {
        return new Theme(name, description, thumbnailUrl);
    }
}
