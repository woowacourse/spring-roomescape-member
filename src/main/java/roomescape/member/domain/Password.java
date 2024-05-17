package roomescape.member.domain;

import java.util.Objects;
import roomescape.global.exception.exceptions.InvalidInputException;

public class Password {

    private static final String PASSWORD_FORMAT = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@!%*#?&])[a-zA-Z\\d@!%*#?&]{6,}$";

    private final String password;

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidInputException("비밀번호가 입력되지 않았습니다. 6자 이상 20자 이하로 입력해주세요.");
        }
        if (password.length() < 6 || password.length() > 20) {
            throw new InvalidInputException(password + "은 유효하지 않은 비밀번호입니다. 6자 이상 20자 이하로 입력해주세요.");
        }
        if (!password.matches(PASSWORD_FORMAT)) {
            throw new InvalidInputException(password + "은 유효하지 않은 비밀번호입니다. 영문과 숫자, 특수문자(@!%*#?&)만을 모두 포함해야 합니다.");
        }
    }

    public String getValue() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String toString() {
        return "Password{" +
                "password='" + password + '\'' +
                '}';
    }
}
