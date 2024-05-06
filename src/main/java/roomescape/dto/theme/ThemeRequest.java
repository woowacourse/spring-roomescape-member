package roomescape.dto.theme;

public record ThemeRequest(String name, String description, String thumbnail) {

    public ThemeRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 입력되지 않았습니다.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("설명이 입력되지 않았습니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("썸네일이 입력되지 않았습니다.");
        }
    }
}
