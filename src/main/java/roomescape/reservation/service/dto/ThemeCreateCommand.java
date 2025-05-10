package roomescape.reservation.service.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.reservation.domain.Theme;

public record ThemeCreateCommand(
        @NotNull(message = "테마명을 입력해주세요.") String name,
        @NotNull(message = "테마소개를 입력해주세요.") String description,
        @NotNull(message = "썸네일을 입력해주세요.") String thumbnail
) {

    public Theme convertToTheme() {
        return new Theme(null, name, description, thumbnail);
    }
}
