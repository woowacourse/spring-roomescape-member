package roomescape.login.domain;

import lombok.Getter;

@Getter
public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(final Long id, final String name, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
