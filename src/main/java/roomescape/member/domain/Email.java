package roomescape.member.domain;

public class Email {
    private static final String EMAIL_SIGN = "@";

    private final String email;

    public Email(String email) {
        validateEmail(email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    private void validateEmail(String email) {
        if (!email.contains(EMAIL_SIGN)) {
            throw new IllegalArgumentException("이메일의 형식에 맞게 작성해주세요.");
        }
    }
}

