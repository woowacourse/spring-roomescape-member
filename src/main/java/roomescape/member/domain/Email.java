package roomescape.member.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final String value;

    public Email(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        validateBlank(value);
        validateEmailPattern(value);
    }

    private void validateBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("값이 존재하지 않습니다.");
        }
    }

    private void validateEmailPattern(final String value) {
        if (!EMAIL_PATTERN.matcher(value)
                .matches()
        ) {
            throw new IllegalArgumentException("%s는 이메일 형식이 아닙니다.".formatted(value));
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
        if (!(o instanceof Email email)) {
            return false;
        }
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
