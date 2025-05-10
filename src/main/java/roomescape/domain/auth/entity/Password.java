package roomescape.domain.auth.entity;

import java.util.Objects;
import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.service.PasswordEncryptor;

@Getter
public class Password {

    private static final int MAX_PASSWORD_LENGTH = 25;
    private final String encryptedPassword;

    private Password(final String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
        validate();
    }

    public static Password encrypt(final String rawPassword, final PasswordEncryptor passwordEncryptor) {
        validateRawPassword(rawPassword);
        return new Password(passwordEncryptor.encrypt(rawPassword));
    }

    public static Password of(final String encryptedPassword) {
        return new Password(encryptedPassword);
    }

    public static void validateRawPassword(final String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank() || rawPassword.length() > MAX_PASSWORD_LENGTH) {
            throw new InvalidArgumentException("패스워드 는 null이거나 25자 이상일 수 없습니다.");
        }
    }

    public void validate() {
        if (encryptedPassword == null || encryptedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }

    }

    public boolean matches(final String rawPassword, final PasswordEncryptor encryptor) {
        return this.encryptedPassword.equals(encryptor.encrypt(rawPassword));
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Password password = (Password) o;
        return Objects.equals(encryptedPassword, password.encryptedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(encryptedPassword);
    }
}
