package roomescape.domain.member;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberEmail {

    private static final String EMAIL_REGEX = "^(.+)@(\\S+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String value;

    public MemberEmail(String value) {
        validateEmailPattern(value);
        this.value = value;
    }

    private void validateEmailPattern(String value) {
        Matcher matcher = EMAIL_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
