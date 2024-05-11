package roomescape.domain.member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public Email {
        validateNotNull(value);
        validateEmailFormat(value);
    }

    private void validateNotNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("메일 주소는 null이 될 수 없습니다.");
        }
    }

    private void validateEmailFormat(String value) {
        Matcher matcher = EMAIL_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    String.format("메일 주소 형식이 올바르지 않습니다. (현재 입력한 메일 주소: %s)", value));
        }
    }
}
