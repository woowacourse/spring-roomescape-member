package roomescape.login.domain;

import lombok.Getter;
import roomescape.exception.ReservationException;

@Getter
public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(final Long id, final String name, final String email, final String password, final Role role) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = null;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new ReservationException("Name cannot be null or blank");
        }
    }
}
