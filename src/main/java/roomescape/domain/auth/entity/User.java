package roomescape.domain.auth.entity;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.exception.InvalidAuthorizationException;
import roomescape.domain.auth.service.PasswordEncryptor;

@Getter
public class User {

    private static final String EMAIL_REGEX = "^[a-z+-_]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    private final Long id;
    private final Name username;
    private final String email;
    private final Password password;
    private final Roles role;

    @Builder
    public User(final Long id, final Name username, final String email, final Password password, final Roles role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;

        validate();
    }

    public static User withoutId(final Name name, final String email, final Password password, final Roles role) {
        return new User(null, name, email, password, role);
    }

    private void validate() {
        if (username == null || role == null || password == null) {
            throw new InvalidArgumentException("User 필드는 null일 수 없습니다. (id 필드 제외)");
        }

        validateEmail();
    }

    private void validateEmail() {
        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new InvalidArgumentException("잘못된 이메일입니다.");
        }
    }

    public void login(final String email, final String rawPassword, final PasswordEncryptor passwordEncryptor) {
        if (!Objects.equals(email, this.email) || !password.matches(rawPassword, passwordEncryptor)) {
            throw new InvalidAuthorizationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean existId() {
        return id != null;
    }

    public String getName() {
        return username.getName();
    }

    public boolean isNotAdmin() {
        return role.isNotAdmin();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User user = (User) o;
        return Objects.equals(id, user.id);
    }
}
