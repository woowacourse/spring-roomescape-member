package roomescape.domain;

import io.micrometer.common.util.StringUtils;
import java.util.Objects;

public class MemberPassword {

    private final String value;

    public MemberPassword(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
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
        MemberPassword that = (MemberPassword) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
