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

    private Long id;
    private final String name;
    private final UserRole role;
    private final String email;
    private final String password;

    public User(final String name, final UserRole role, final String email, final String password) {
        this(null, name, role, email, password);
    }

    public User(final Long id, final String name, final UserRole role, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public boolean matchesPassword(String passwordToCompare) {
        return password.equals(passwordToCompare);
    }
}
