package roomescape.member.domain;

public class Email {
    private static final int MAX_EMAIL_LENGTH = 20;
    private final String value;

    public Email(final String value) {
        validateEmail(value);
        this.value = value;
    }

    private void validateEmail(final String value) {
        if (value.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 이메일은 " + MAX_EMAIL_LENGTH + "자 이하로 입력해주세요.");
        }
    }

    public String getValue() {
        return value;
    }
}
