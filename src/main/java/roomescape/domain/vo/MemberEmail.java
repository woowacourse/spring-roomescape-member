package roomescape.domain.vo;

import io.micrometer.common.util.StringUtils;
import java.util.Objects;
import java.util.regex.Pattern;

public class MemberEmail {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-z0-9]+@[A-z0-9.-]+\\.[A-z]{2,6}$");
    private final String value;

    public MemberEmail(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (StringUtils.isBlank(value) || !VALID_EMAIL_ADDRESS_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException("이메일 형식이 아닙니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberEmail memberEmail = (MemberEmail) o;
        return Objects.equals(value, memberEmail.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
