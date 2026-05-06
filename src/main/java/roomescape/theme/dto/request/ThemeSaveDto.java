package roomescape.theme.dto.request;

import roomescape.common.validation.annotation.NotBlank;
import roomescape.common.validation.annotation.NotNull;

public record ThemeSaveDto(

        @NotBlank(message = "name은 비어있을 수 없습니다.")
        String name,

        @NotBlank(message = "description은 비어있을 수 없습니다.")
        String description,

        @NotNull
        String thumbnailUrl
) {
}
