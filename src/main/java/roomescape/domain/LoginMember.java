package roomescape.domain;

public class LoginMember {

    private final Long id;
    private final String name;
    private final String email;
    private final MemberRole role;

    public LoginMember(final Long id, final String name, final String email, final MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public boolean isAdmin() {
        return this.role.isAdmin();
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

    public MemberRole getRole() {
        return role;
    }
}
