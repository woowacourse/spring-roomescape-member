package roomescape.domain.member;

import java.util.Objects;

public record Name(String value) {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 5;

    public Name {
        validateNonNull(value);
        validateLength(value);
    }

    private void validateNonNull(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름 길이는 %d 이하, %d 이상만 가능합니다", MIN_LENGTH, MAX_LENGTH));
        }
    }
}
