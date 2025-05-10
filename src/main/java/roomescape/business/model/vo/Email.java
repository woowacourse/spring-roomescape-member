package roomescape.business.model.vo;

import roomescape.exception.business.InvalidCreateArgumentException;

import java.util.regex.Pattern;

public record Email(
        String value
) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\p{L}0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public Email {
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidCreateArgumentException("이메일 형식이어야 합니다.");
        }
    }
}
