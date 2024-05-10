package roomescape.infrastructure;

import roomescape.exception.ClientErrorExceptionWithLog;

public class PasswordEncoder {

    public String encode(String password) {
        validateEmpty(password);
        return String.valueOf(password.hashCode());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        validateEmpty(rawPassword);
        String loginPassword = String.valueOf(rawPassword.hashCode());
        return loginPassword.equals(encodedPassword);
    }

    private void validateEmpty(String password) {
        if (password == null) {
            throw new ClientErrorExceptionWithLog("[ERROR] 비밀번호가 없습니다.");
        }
    }
}
