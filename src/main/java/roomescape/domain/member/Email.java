package roomescape.domain.member;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
    );

    public Email {
        validateNonNull(value);
        validateFormat(value);
    }

    private void validateNonNull(String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("이메일은 null이 될 수 없습니다.");
        }
    }

    private void validateFormat(String value) {
        if (!EMAIL_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException(String.format("입력 값: %s,이메일 형식에 맞추어 입력해주세요", value));
        }
    }
}
