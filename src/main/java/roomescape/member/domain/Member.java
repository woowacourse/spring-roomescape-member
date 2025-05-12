package roomescape.member.domain;

import roomescape.auth.domain.Role;
import roomescape.common.domain.Id;

public class Member {
    private final Id id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(final Id id, final String name, final String email, final String password, final Role role) {
        validateNameLength(name);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member of(final Long id, final String name, final String email, final String password,
                            final Role role) {
        return new Member(Id.from(id), name, email, password, role);
    }

    public static Member withUnassignedId(final String name, final String email, final String password,
                                          final Role role) {
        return new Member(Id.unassigned(), name, email, password, role);
    }

    private void validateNameLength(final String value) {
        if (value.length() > 10) {
            throw new IllegalArgumentException("이름은 10글자 이내여야 합니다.");
        }
    }

    public Long getId() {
        return id.getValue();
    }

    public void setId(final Long value) {
        id.setValue(value);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
