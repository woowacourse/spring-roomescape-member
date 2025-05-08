package roomescape.domain.user;

import java.util.Objects;
import java.util.regex.Pattern;

public record UserEmail(String email) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public UserEmail(final String email) {
        this.email = Objects.requireNonNull(email, "email은 null이 아니어야 합니다.");

        if (email.isBlank()) {
            throw new IllegalStateException("email은 공백일 수 없습니다.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalStateException("올바른 이메일 형식이 아닙니다: " + email);
        }

        if (email.length() > 40) {
            throw new IllegalStateException("email은 40자 이하여야 합니다.");
        }
    }
}
