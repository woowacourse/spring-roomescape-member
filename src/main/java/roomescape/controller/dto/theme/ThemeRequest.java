package roomescape.controller.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import roomescape.service.dto.CreateThemeCommand;

public record ThemeRequest(
        @NotBlank(message = "테마 이름은 필수입니다.")
        @Size(max = 255, message = "테마 이름은 255자를 초과할 수 없습니다.")
        String name,

        @NotBlank(message = "테마 설명은 필수입니다.")
        @Size(max = 255, message = "테마 설명은 255자를 초과할 수 없습니다.")
        String description,

        @NotBlank(message = "테마 이미지 URL은 필수입니다.")
        @Size(max = 512, message = "이미지 URL은 512자를 초과할 수 없습니다.")
        String imageUrl
) {

    public CreateThemeCommand toCommand() {
        return new CreateThemeCommand(
                name.trim(),
                description.trim(),
                imageUrl.trim()
        );
    }
}
