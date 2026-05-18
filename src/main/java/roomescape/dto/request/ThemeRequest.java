package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import roomescape.domain.Theme;

public record ThemeRequest(
        @NotBlank(message = "테마 이름은 비어 있을 수 없습니다.")
        String name,

        @NotBlank(message = "테마 설명은 비어 있을 수 없습니다.")
        String description,

        @URL(message = "URL 형식이 올바르지 않습니다.")
        @NotBlank(message = "썸네일 URL은 비어 있을 수 없습니다.")
        String thumbnailUrl
) {
    public Theme domain() {
        return new Theme(null, name, description, thumbnailUrl);
    }
}
