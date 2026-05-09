package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인기 테마 응답")
public record PopularThemeResponse(
        @Schema(description = "테마 ID", example = "1")
        Long id,

        @Schema(description = "테마 이름", example = "공포의 방")
        String name,

        @Schema(description = "테마 설명", example = "무서운 방탈출 테마입니다.")
        String description,

        @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.png")
        String thumbnailImageUrl,

        @Schema(description = "최근 1주 예약 건수", example = "42")
        Long reservedCount
) {
}
