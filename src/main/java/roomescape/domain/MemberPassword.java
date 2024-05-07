package roomescape.domain;

import io.micrometer.common.util.StringUtils;
import java.util.Objects;

public class MemberPassword {

    private final String password;

    public MemberPassword(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(final String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberPassword that = (MemberPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
