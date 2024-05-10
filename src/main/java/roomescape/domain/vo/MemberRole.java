package roomescape.domain.vo;

import io.micrometer.common.util.StringUtils;
import java.util.Objects;

public class MemberRole {

    private static final int MAX_ROLE_SIZE = 10;
    private final String value;

    public MemberRole(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (StringUtils.isBlank(value) || value.length() > MAX_ROLE_SIZE) {
            throw new IllegalArgumentException("사용자의 권한은 1글자 이상 10글자 이하 입니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberRole that = (MemberRole) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
