package roomescape.domain;

import roomescape.exception.BadRequestException;

public class Member {

    private static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

    private final String name;
    private final String email;
    private final String password;

    public Member(String name, String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("이름에 빈값을 입력할 수 없습니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("이메일에 빈값을 입력할 수 없습니다.");
        }

        if (!email.matches(EMAIL_PATTERN)) {
            throw new BadRequestException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("비밀번호에 빈값을 입력할 수 없습니다.");
        }
    }
}
