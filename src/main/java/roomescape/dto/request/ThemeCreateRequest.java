package roomescape.dto.request;

import java.util.Arrays;

public record ThemeCreateRequest(
        String name,
        String description,
        String thumbnail
) {

    public ThemeCreateRequest {
        validateBlank(name, description, thumbnail);
    }

    private void validateBlank(final String name, final String description, final String thumbnail) {
        if (isNotValidInput(name, description, thumbnail)) {
            throw new IllegalArgumentException("빈 값으로 예약할 수 없습니다.");
        }
    }

    private boolean isNotValidInput(final String... inputs) {
        return Arrays.stream(inputs)
                .anyMatch(input -> input == null || input.isBlank());
    }
}
