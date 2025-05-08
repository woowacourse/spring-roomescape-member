package roomescape.domain.user;

import java.util.Objects;
import java.util.regex.Pattern;

public record UserPassword(String password) {

    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[A-Za-z]");

    public UserPassword(final String password) {
        this.password = Objects.requireNonNull(password, "password은 null일 수 없습니다.");

        if (password.isBlank()) {
            throw new IllegalStateException("비밀번호는 공백일 수 없습니다.");
        }

        if (password.length() > 30) {
            throw new IllegalStateException("비밀번호는 30자를 초과할 수 없습니다.");
        }

        long digitCount = DIGIT_PATTERN.matcher(password).results().count();
        long specialCharCount = SPECIAL_CHAR_PATTERN.matcher(password).results().count();
        long letterCount = LETTER_PATTERN.matcher(password).results().count();

        if (digitCount < 2) {
            throw new IllegalStateException("비밀번호에는 숫자가 최소 2자 이상 포함되어야 합니다. (현재: " + digitCount + ")");
        }

        if (specialCharCount < 1) {
            throw new IllegalStateException("비밀번호에는 특수문자가 최소 1자 이상 포함되어야 합니다. (현재: " + specialCharCount + ")");
        }

        if (letterCount < 5) {
            throw new IllegalStateException("비밀번호에는 영문자가 최소 5자 이상 포함되어야 합니다. (현재: " + letterCount + ")");
        }
    }
}
