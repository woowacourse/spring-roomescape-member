package roomescape.domain.member;

import roomescape.exception.EmptyParameterException;
import roomescape.exception.ParameterException;

public class Password {
    private static final int PASSWORD_MINIMUM_LENGTH = 5;
    private static final int PASSWORD_MAXIMUM_LENGTH = 20;

    private final String rawPassword;

    public Password(String rawPassword) {
        validatePasswordExist(rawPassword);
        validatePasswordLength(rawPassword);

        this.rawPassword = rawPassword;
    }

    private void validatePasswordLength(String password) {
        int passwordLength = password.length();
        if (passwordLength < PASSWORD_MINIMUM_LENGTH || passwordLength > PASSWORD_MAXIMUM_LENGTH) {
            String errorMessage = String.format("비밀번호는 %d자 이상, %d자 이하여야 합니다.",
                    PASSWORD_MINIMUM_LENGTH, PASSWORD_MAXIMUM_LENGTH);
            throw new ParameterException(errorMessage);
        }
    }

    private void validatePasswordExist(String password) {
        if (password == null || password.isBlank()) {
            throw new EmptyParameterException("비밀번호가 비어 있습니다.");
        }
    }

    public boolean matches(String value) {
        return rawPassword.equals(value);
    }

    public String getPassword() {
        return rawPassword;
    }
}
