package roomescape.business.domain.member;

import java.util.regex.Pattern;
import roomescape.exception.MemberException;

public record Email(
        String value
) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new MemberException("이메일은 null이거나 빈 문자열일 수 없습니다.");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new MemberException("이메일 형식이 올바르지 않습니다.");
        }
    }
}
