package roomescape.domain.theme.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ThemeCreateRequestDto(@NotBlank(message = "테마 이름을 입력해주세요.") String name,
                                    @NotBlank(message = "테마 설명을 입력해주세요.") String description,
                                    @NotBlank(message = "테마 이미지 URL을 입력해주세요.") String imageUrl) {

}
