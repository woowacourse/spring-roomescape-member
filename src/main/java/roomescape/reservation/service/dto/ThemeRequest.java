package roomescape.reservation.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ThemeRequest(
        @NotBlank(message = "이름은 빈칸(공백)일 수 없습니다.") String name,
        @NotNull String description,
        @Pattern(regexp = "^$|(https?|ftp)://.*\\.(jpeg|jpg|png|gif|bmp)$", message = "올바르지 않은 썸네일 형식입니다.") String thumbnail
) {
}
