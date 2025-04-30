package roomescape.dto;

public record ThemeRequest(
    // TODO : valid 사항 표시해주기
    String name,
    String description,
    String thumbnail
) {
}
