package roomescape.domain;

public class LoginMember {

    private final long id;
    private final String name;
    private final String email;
    private final MemberRoleType role;

    public LoginMember(final long id, final String name, final String email, final MemberRoleType role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSameRole(MemberRoleType role) {
        return this.role.equals(role);
    }
}
