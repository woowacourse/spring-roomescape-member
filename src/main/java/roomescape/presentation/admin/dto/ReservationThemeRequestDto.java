package roomescape.presentation.admin.dto;

import jakarta.validation.constraints.NotEmpty;

public record ReservationThemeRequestDto(
        @NotEmpty(message = "추가할 테마 이름은 필수입니다.") String name,
        @NotEmpty(message = "추가할 테마 설명은 필수입니다.") String description,
        @NotEmpty(message = "추가할 테마 썸네일 URL은 필수입니다.") String thumbnail
) {
}
