package roomescape.domain.theme;

import java.util.Objects;

public record Name(String name) {
    private static final int MAX_LENGTH = 10;

    public Name {
        validateNull(name);
        validateLength(name);
    }

    private void validateNull(final String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름 값은 null이 될 수 없습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.isEmpty() || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("테마이름은 %d자까지 만 가능합니다.", MAX_LENGTH));
        }
    }
}
