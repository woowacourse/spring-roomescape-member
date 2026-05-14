package roomescape.theme.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ThemeSaveDto(

        @NotBlank(message = "name은 비어있을 수 없습니다.")
        String name,

        @NotBlank(message = "description은 비어있을 수 없습니다.")
        String description,

        @NotNull(message = "thumbnailUrl은 필수 입력값입니다.")
        String thumbnailUrl
) {
}
