package roomescape.utils;

import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.service.PasswordEncryptor;
import roomescape.infrastructure.security.Sha256PasswordEncryptor;

public final class PasswordFixture {

    private PasswordFixture() {
    }

    private static final PasswordEncryptor passwordEncryptor = new Sha256PasswordEncryptor();

    public static Password generate() {
        return Password.encrypt("password", passwordEncryptor);
    }

    public static Password generate(final String password) {
        return Password.encrypt(password, passwordEncryptor);
    }
}
