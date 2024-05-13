package roomescape.domain.member;

import java.util.regex.Pattern;

public class Email {
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 30;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private final String email;

    protected Email(String email) {
        validateBlank(email);
        validateLength(email);
        validatePattern(email);
        this.email = email;
    }

    private void validateBlank(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일이 비어있습니다.");
        }
    }

    private void validateLength(String email) {
        if (email.length() < MIN_LENGTH || MAX_LENGTH < email.length()) {
            throw new IllegalArgumentException(
                    String.format("이메일은 %d자 이상, %d자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    private void validatePattern(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    protected String getValue() {
        return email;
    }
}
