package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ThemeCreationRequest(
        @NotBlank(message = "[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.")
        String name,
        @NotBlank(message = "[ERROR] 설명은 빈 값이나 공백값을 허용하지 않습니다.")
        String description,
        @NotBlank(message = "[ERROR] 썸네일은 빈 값이나 공백값을 허용하지 않습니다.")
        String thumbnail) {
}
