package roomescape.domain;

import java.util.regex.Pattern;

public class Email {

    private static final String REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(REGEX);

    private final String email;

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String value) {
        if (value == null || value.isBlank() || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("잘못된 형식의 이메일입니다. " + value);
        }
    }

    public String getEmail() {
        return email;
    }
}
