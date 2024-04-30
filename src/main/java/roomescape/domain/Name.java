package roomescape.domain;

import java.util.Objects;

public record Name(String value) {
    private static final int NAME_LENGTH_MIN = 2;
    private static final int NAME_LENGTH_MAX = 10;

    public Name {
        validateLength(value);
    }

    public static void validateLength(String value) {
        Objects.requireNonNull(value);
        if (value.length() < NAME_LENGTH_MIN || NAME_LENGTH_MAX < value.length()) {
            throw new IllegalArgumentException(String.format("사용자 이름은 %d자 이상 %d자 이하여야 합니다. (현재 입력한 이름 길이: %d자)",
                    NAME_LENGTH_MIN, NAME_LENGTH_MAX, value.length()));
        }
    }
}
