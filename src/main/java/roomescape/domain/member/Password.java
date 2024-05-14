package roomescape.domain.member;

import roomescape.exception.InvalidMemberException;

import java.util.Objects;

public class Password {
    private static final int MINIMUM_PASSWORD_LENGTH = 6;
    private static final int MAXIMUM_PASSWORD_LENGTH = 12;

    private final String password;

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        if (password.length() < MINIMUM_PASSWORD_LENGTH || password.length() > MAXIMUM_PASSWORD_LENGTH) {
            throw new InvalidMemberException("비밀번호는 6자 이상 12자 이하여야 합니다.");
        }
    }

    public boolean isSame(String password) {
        return this.password.equals(password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(this.password, password.password);
    }

    public String getPassword() {
        return password;
    }
}
