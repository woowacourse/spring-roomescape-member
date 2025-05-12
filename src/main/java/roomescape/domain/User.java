package roomescape.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(of = {"id"})
@Getter
@Accessors(fluent = true)
@ToString
public class User {

    private static final int NAME_MAX_LENGTH = 5;
    private static final int PASSWORD_MAX_LENGTH = 30;
    private static final String VALID_EMAIL_FORMAT = "\\w+@\\w+\\.\\w+";

    private Long id;
    private final String name;
    private final UserRole role;
    private final String email;
    private final String password;

    public User(final Long id, final String name, final UserRole role, final String email, final String password) {
        validateNameLength(name);
        validateEmailFormat(email);
        validatePasswordLength(password);
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public User withId(final long id) {
        if (this.id == null) {
            this.id = id;
            return this;
        }
        throw new IllegalStateException("사용자 ID는 재할당할 수 없습니다. 현재 ID: " + this.id);
    }

    public boolean matchesPassword(final String passwordToCompare) {
        return password.equals(passwordToCompare);
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public static User createUser(final String name, final String email, final String password) {
        return new User(null, name, UserRole.USER, email, password);
    }

    private void validateNameLength(final String name) {
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 공백이거나 %d자를 넘길 수 없습니다.", NAME_MAX_LENGTH));
        }
    }

    private void validateEmailFormat(final String email) {
        if (email.matches(VALID_EMAIL_FORMAT)) {
            return;
        }
        throw new IllegalArgumentException("잘못된 형식의 이메일입니다 : " + email);
    }

    private void validatePasswordLength(final String password) {
        if (password.isBlank() || password.length() > PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("비밀번호는 공백이거나 %d자를 넘길 수 없습니다.", PASSWORD_MAX_LENGTH));
        }
    }
}
