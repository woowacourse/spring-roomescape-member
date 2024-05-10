package roomescape.domain.member;

public class Password {
    private static final int MIN_LENTH = 5;
    private static final int MAX_LENTH = 10;

    private final String password;

    public Password(String password) {
        validatePassword(password);
        this.password = password;
    }

    private void validatePassword(String password) {
        if (password.isBlank() || password.length() < MIN_LENTH || password.length() > MAX_LENTH) {
            throw new IllegalArgumentException("Password는 " + MIN_LENTH + "자 이상 " + MAX_LENTH + "자 이하여야 합니다.");
        }
    }

    public String getPassword() {
        return password;
    }
}
