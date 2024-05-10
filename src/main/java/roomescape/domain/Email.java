package roomescape.domain;

import roomescape.exception.InvalidInputException;

public class Email {

    private static final String EMAIL_FORMAT = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final String email;

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("이메일이 입력되지 않았습니다. \"user@example.com\" 의 형식으로 입력해주세요.");
        }
        if (!email.matches(EMAIL_FORMAT)) {
            throw new InvalidInputException(email + "은 유효하지 않은 이메일입니다. \"user@example.com\" 의 형식으로 입력해주세요.");
        }
    }

    public String getEmail() {
        return email;
    }
}
