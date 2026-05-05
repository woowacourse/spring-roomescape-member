package roomescape.theme.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record ThemeRequest(

        @NotBlank(message = "테마 이름은 필수입니다.")
        String name,

        String description,
        String imageUrl,

        @NotBlank(message = "테마 소요 시간은 필수입니다.")
        LocalTime requiredTime
) {
}
