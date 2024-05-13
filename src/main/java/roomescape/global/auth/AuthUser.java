package roomescape.global.auth;

import roomescape.member.domain.Role;

import java.util.Objects;

public class AuthUser {
    private final Long id;
    private final String name;
    private final Role role;

    public AuthUser(Long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthUser authUser = (AuthUser) o;
        return Objects.equals(id, authUser.id) && Objects.equals(name, authUser.name) && role == authUser.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, role);
    }
}
