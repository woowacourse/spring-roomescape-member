package roomescape.domain;

public class Password {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 12;

    private final String value;

    public Password(String value) {
        validatePasswordLength(value);
        validateIncludeWhiteSpace(value);

        this.value = value;
    }

    private void validatePasswordLength(String value) {
        if (value.length() < MIN_PASSWORD_LENGTH || value.length() > MAX_PASSWORD_LENGTH) {
            String message = String.format(
                    "비밀번호의 길이는 %d ~ %d글자 사이입니다.",
                    MIN_PASSWORD_LENGTH,
                    MAX_PASSWORD_LENGTH
            );
            throw new IllegalArgumentException(message);
        }
    }

    private void validateIncludeWhiteSpace(String value) {
        boolean hasWhiteSpace = value.chars().anyMatch(Character::isWhitespace);
        if (hasWhiteSpace) {
            throw new IllegalArgumentException("비밀번호는 공백을 포함할 수 없습니다.");
        }
    }

    public String value() {
        return "*".repeat(value.length());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password = (Password) o;

        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
