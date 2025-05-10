package roomescape.member.domain;

import java.util.Objects;

public class MemberName {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    private final String value;

    public MemberName(final String value) {
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
        int length = value.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new IllegalArgumentException("사용자 이름은 %d자 이상 %d자 이하로 가능합니다.".formatted(
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
        if (!(o instanceof MemberName that)) {
            return false;
        }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
