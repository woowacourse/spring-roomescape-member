package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import roomescape.domain.Theme;

public record ThemeResponse(
        @Schema(description = "테마 ID", example = "1")
        long id,

        @Schema(description = "테마 이름", example = "공포")
        String name,

        @Schema(description = "테마 설명", example = "이 테마는 XX")
        String description,

        @Schema(description = "테마 썸네일 주소", example = "https::/url")
        String thumbnail
) {

    public static ThemeResponse from(final Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }
}
