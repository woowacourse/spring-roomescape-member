package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.reservation.Theme;

public record ThemeRequest(
        @NotBlank(message = "테마의 이름을 입력해주세요") String name,
        @NotBlank(message = "테마의 설명을 입력햐주세요") String description,
        @NotBlank(message = "테마 썸네일의 주소를 입력해주세요") String thumbnail
) {
    public Theme toEntity() {
        return new Theme(null, name, description, thumbnail);
    }
}
