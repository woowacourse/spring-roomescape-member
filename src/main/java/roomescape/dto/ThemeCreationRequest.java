package roomescape.dto;

import java.time.LocalDate;

public record ThemeCreationRequest(String name, String description, String thumbnail) {

    public ThemeCreationRequest {
        validate(name, description, thumbnail);
    }

    public void validate(String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 설명은 빈 값이나 공백값을 허용하지 않습니다.");
        }
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 썸네일은 빈 값이나 공백값을 허용하지 않습니다.");
        }
    }
}
