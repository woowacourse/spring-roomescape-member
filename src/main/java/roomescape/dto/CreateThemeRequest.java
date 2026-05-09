package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "테마 생성 요청")
public record CreateThemeRequest(
        @Schema(description = "테마 이름", example = "공포의 방")
        String name,

        @Schema(description = "테마 설명", example = "무서운 방탈출 테마입니다.")
        String description,

        @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.png")
        String thumbnailImageUrl
) {
}
