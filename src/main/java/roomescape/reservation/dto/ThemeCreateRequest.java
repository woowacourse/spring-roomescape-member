package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Theme;

public record ThemeCreateRequest(
        @NotBlank(message = "테마 이름은 필수 입력값 입니다.") String name,
        @NotBlank(message = "설명은 필수 입력값 입니다.") String description,
        @NotBlank(message = "사진은 필수 입력값 입니다.") String thumbnail
) {

    public Theme toTheme() {
        return new Theme(new Name(name), description, thumbnail);
    }
}
