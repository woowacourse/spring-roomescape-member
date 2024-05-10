package roomescape.reservation.dto.theme;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest(
        @NotBlank(message = "이름이 입력되지 않았습니다.") String name,
        @NotBlank(message = "설명이 입력되지 않았습니다.") String description,
        @NotBlank(message = "썸네일이 입력되지 않았습니다.") String thumbnail
) {
}
