package roomescape.dto;

public record ThemeRequestDto(
    // TODO : valid 사항 표시해주기
    String name,
    String description,
    String thumbnail
) {
}
