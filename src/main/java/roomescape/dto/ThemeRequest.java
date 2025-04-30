package roomescape.dto;

import roomescape.exception.InvalidInputException;

public record ThemeRequest(String name, String description, String thumbnail) {

    public ThemeRequest {
        validateNull(name, description, thumbnail);
        validateLength(name, description, thumbnail);
    }

    private void validateNull(String name, String description, String thumbnail) {
        if(name == null || description == null || thumbnail == null) {
            throw new InvalidInputException("값을 모두 선택해라.");
        }
    }

    private void validateLength(String name, String description, String thumbnail) {
        if (name.isBlank() || description.isBlank() || thumbnail.isBlank()) {
            throw new InvalidInputException("입력되지 않은 값이 있다.");
        }
    }
}
