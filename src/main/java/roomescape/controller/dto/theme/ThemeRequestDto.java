package roomescape.controller.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.service.command.ThemeCommand;

public record ThemeRequestDto(
        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        String description,

        @NotBlank(message = "url은 필수 입력값입니다.")
        String imageUrl
) {
        public ThemeCommand toCommand() {
                return new ThemeCommand(
                        ThemeName.from(name),
                        description,
                        ThemeImageUrl.from(imageUrl)
                );
        }
}
