package roomescape.domain.member;

import java.util.regex.Pattern;

public class Password {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+-={}\\[\\];:'\",.<>/?]*$");

    private final String password;

    protected Password(String password) {
        validateBlank(password);
        validateLength(password);
        validateContainsBlank(password);
        validatePattern(password);
        this.password = password;
    }

    private void validateBlank(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호가 비어있습니다.");
        }
    }

    private void validateLength(String password) {
        if (password.length() < MIN_LENGTH || MAX_LENGTH < password.length()) {
            throw new IllegalArgumentException(
                    String.format("비밀번호는 %d글자 이상, %d글자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    protected void validateContainsBlank(String password) {
        if (password.contains(" ")) {
            throw new IllegalArgumentException("비밀번호에 공백이 포함되어 있습니다.");
        }
    }

    private void validatePattern(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("비밀번호는 영어, 숫자, 특수문자만 가능합니다.");
        }
    }

    protected boolean match(String password) {
        return this.password.equals(password);
    }

    protected String getValue() {
        return this.password;
    }
}
