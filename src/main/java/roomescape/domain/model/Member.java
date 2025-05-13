package roomescape.domain.model;

import java.util.Objects;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(final Long id, final String name, final String email, final String password, final String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.fromName(role);
    }

    public Member(final String name, final String email, final String password, final String role) {
        this(null, name, email, password, role);
    }

    public Member(final Long id, final String name, final String email, final String role) {
        this(id, name, email, null, role);
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Member user = (Member) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
