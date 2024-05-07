package roomescape.domain;

import io.micrometer.common.util.StringUtils;
import java.util.Objects;
import java.util.regex.Pattern;

public class UserEmail {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-z0-9]+@[A-z0-9.-]+\\.[A-z]{2,6}$");
    private final String email;

    public UserEmail(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        if (StringUtils.isBlank(email) || !VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 아닙니다.");
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(email, userEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
