package roomescape.domain.member;

import java.util.Objects;

public record Password(String value) {
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 12;

    public Password {
        validateNonNull(value);
        validateLength(value);
    }

    private void validateNonNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("비밀번호는 null이 될 수 없습니다.");
        }
    }

    private void validateLength(final String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("비밀번호 길이는 %d 이하, %d 이상만 가능합니다", MIN_LENGTH, MAX_LENGTH));
        }
    }
}
