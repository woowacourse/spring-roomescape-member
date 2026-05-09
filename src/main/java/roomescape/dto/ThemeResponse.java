package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import roomescape.domain.Theme;

@Schema(description = "테마 응답")
public record ThemeResponse(
        @Schema(description = "테마 ID", example = "1")
        Long id,

        @Schema(description = "테마 이름", example = "공포의 방")
        String name,

        @Schema(description = "테마 설명", example = "무서운 방탈출 테마입니다.")
        String description,

        @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.png")
        String thumbnailImageUrl
) {
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailImageUrl()
        );
    }
}
