package roomescape.member.domain;

import java.util.Objects;

public class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 16;

    private final String value;

    public Password(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        validateBlank(value);
        validateLength(value);
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("값이 존재하지 않습니다.");
        }
    }

    private void validateLength(final String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 %d자 이상 %d자 이하로 가능합니다.".formatted(
                    MIN_LENGTH, MAX_LENGTH
            ));
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Password password)) {
            return false;
        }
        return Objects.equals(getValue(), password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
