package roomescape.domain.member;

import java.util.Objects;

public final class LoginMember {

    private final Long id;
    private final String email;
    private final String name;
    private final Role role;

    public LoginMember(Long id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role.toString();
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (LoginMember) obj;
        return Objects.equals(this.id, that.id) &&
            Objects.equals(this.email, that.email) &&
            Objects.equals(this.name, that.name) &&
            Objects.equals(this.role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, role);
    }
}
