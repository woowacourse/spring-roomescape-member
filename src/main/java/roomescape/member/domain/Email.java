package roomescape.member.domain;

import java.util.Objects;

public class Email {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final String email;

    public Email(String email) {
        email = Objects.requireNonNull(email);
        validateEmailFormat(email);

        this.email = email;
    }

    private static void validateEmailFormat(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("이메일 형식이 맞지 않습니다.");
        }
    }

    public String getEmail() {
        return email;
    }
}
