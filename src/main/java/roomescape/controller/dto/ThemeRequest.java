package roomescape.controller.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.service.reservation.Theme;

public record ThemeRequest(
        @NotNull(message = "테마명을 입력해주세요.") String name,
        @NotNull(message = "테마소개를 입력해주세요.") String description,
        @NotNull(message = "썸네일을 입력해주세요.") String thumbnail
) {

    public Theme convertToTheme() {
        return new Theme(null, name, description, thumbnail);
    }
}
