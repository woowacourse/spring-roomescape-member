package roomescape.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {

    private final Long id;
    private final Name name;
    private final String email;
    private final String password;

    @Builder
    public User(final Long id, final Name name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
