package roomescape.theme.dto;

public record ThemeRequest(String name, String description, String thumbnail) {

    public ThemeRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름을 입력해주세요.");
        }
        if (description == null  || description.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 설명을 입력해주세요.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 썸네일을 입력해주세요.");
        }
    }

}
