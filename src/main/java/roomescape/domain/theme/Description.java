package roomescape.domain.theme;

import java.util.Objects;

public record Description(String description) {
    private static final int MAX_LENGTH = 10;

    public Description {
        validateNull(description);
        validateLength(description);
    }

    private void validateNull(final String description) {
        if (Objects.isNull(description)) {
            throw new IllegalArgumentException("설명 값은 null이 될 수 없습니다.");
        }
    }

    private void validateLength(String description) {
        if (description.length() < MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("설명은 %d글자 이상 입력해주세요.", MAX_LENGTH));
        }
    }
}
