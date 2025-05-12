package roomescape.theme.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeCreateRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotBlank(message = "설명은 필수입니다.")
        String description,
        @NotBlank(message = "섬네일은 필수입니다.")
        String thumbnail
) {

}
