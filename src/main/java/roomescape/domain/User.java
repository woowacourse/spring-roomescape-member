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

    private Long id;
    private final String name;
    private final UserRole role;
    private final String email;
    private final String password;

    public User(final String name, final UserRole role, final String email, final String password) {
        this(null, name, role, email, password);
    }

    public User(final Long id, final String name, final UserRole role, final String email, final String password) {
        validateNameLength(name);
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public boolean matchesPassword(String passwordToCompare) {
        return password.equals(passwordToCompare);
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    private void validateNameLength(final String name) {
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 공백이거나 %d자를 넘길 수 없습니다.", NAME_MAX_LENGTH));
        }
    }
}
