package roomescape.dto.theme.request;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequestDto(

        @NotBlank(message = "테마 이름은 필수로 입력해야 합니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수로 입력해야 합니다.")
        String description,

        @NotBlank(message = "썸네일 주소는 필수로 입력해야 합니다.")
        String thumbnail) {

}
