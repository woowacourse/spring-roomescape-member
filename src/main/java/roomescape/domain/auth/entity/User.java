package roomescape.domain.auth.entity;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.exception.InvalidAuthorizationException;

@Getter
public class User {

    private static final int PASSWORD_MAX_LENGTH = 25;
    private static final String EMAIL_REGEX = "^[a-z+-_]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    private final Long id;
    private final Name username;
    private final String email;
    private final String password;
    private final Roles role;

    @Builder
    public User(final Long id, final Name username, final String email, final String password, final Roles role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        validate();
    }

    private void validate() {
        if (username == null || email == null || password == null || role == null) {
            throw new InvalidArgumentException("User 필드는 null일 수 없습니다. (id 필드 제외)");
        }

        validateEmail();
        validatePassword();
    }

    private void validateEmail() {
        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidArgumentException("잘못된 이메일입니다.");
        }
    }

    private void validatePassword() {
        if (password.isBlank() || password.length() > PASSWORD_MAX_LENGTH) {
            throw new InvalidArgumentException("유효하지 않은 패스워드입니다.");
        }
    }

    public void login(final String email, final String password) {
        if (!Objects.equals(email, this.email) || !Objects.equals(password, this.password)) {
            throw new InvalidAuthorizationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public static User withoutId(final Name name, final String email, final String password, final Roles role) {
        return new User(null, name, email, password, role);
    }

    public boolean existId() {
        return id != null;
    }

    public String getName() {
        return username.getName();
    }

    public String toRole() {
        return role.getRole();
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
