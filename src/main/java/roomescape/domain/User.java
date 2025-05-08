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
    private final String email;
    private final String password;

    public User(final String name, final String email, final String password) {
        this(null, name, email, password);
    }

    public User(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
