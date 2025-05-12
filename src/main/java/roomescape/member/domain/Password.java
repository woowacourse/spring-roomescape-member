package roomescape.member.domain;

import roomescape.member.exception.PasswordException;

public class Password {

    private final String password;

    public Password(String password) {
        validatePasswordIsNonEmpty(password);
        this.password = password;
    }

    private void validatePasswordIsNonEmpty(final String password) {
        if (password == null || password.isEmpty()) {
            throw new PasswordException("비밀번호는 비어있을 수 없습니다.");
        }
    }

    public String getPassword() {
        return password;
    }
}
