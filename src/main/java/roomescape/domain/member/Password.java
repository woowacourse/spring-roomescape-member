package roomescape.domain.member;

import java.util.regex.Pattern;

public record Password(String value) {
    private static final int PASSWORD_LENGTH_MIN = 6;
    private static final int PASSWORD_LENGTH_MAX = 20;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@#$%^&*]{6,20}$");

    public Password {
        validateNotNull(value);
        validatePasswordLength(value);
        validatePasswordPattern(value);
    }

    private void validateNotNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("비밀번호는 null이 될 수 없습니다.");
        }
    }

    private void validatePasswordLength(String value) {
        if (value.length() < PASSWORD_LENGTH_MIN || PASSWORD_LENGTH_MAX < value.length()) {
            throw new IllegalArgumentException(String.format("비밀번호는 %d자 이상 %d자 이하여야 합니다. (현재 입력한 비밀번호 길이: %d자)",
                    PASSWORD_LENGTH_MIN, PASSWORD_LENGTH_MAX, value.length()));
        }
    }

    private void validatePasswordPattern(String value) {
        if (!PASSWORD_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("비밀번호는 영문 대소문자, 숫자, 특수문자(!@#$%^&*)만 사용할 수 있습니다.");
        }
    }
}
