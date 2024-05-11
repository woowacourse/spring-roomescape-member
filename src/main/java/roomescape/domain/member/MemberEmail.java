package roomescape.domain.member;

import java.util.regex.Pattern;
import roomescape.domain.exception.InvalidValueException;

public class MemberEmail {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private final String value;

    public MemberEmail(String value) {
        validateNotNullOrEmpty(value);
        validateEmailFormat(value);
        this.value = value;
    }

    private void validateNotNullOrEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("이메일을 입력해주세요.");
        }
    }

    private void validateEmailFormat(String value) {
        if (!pattern.matcher(value).matches()) {
            throw new InvalidValueException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
