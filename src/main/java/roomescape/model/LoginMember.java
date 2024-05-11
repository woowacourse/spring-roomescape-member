package roomescape.model;

public class LoginMember {

    private final Long id;
    private final MemberName name;
    private final Role role;
    private final String email;

    public LoginMember(final Long id, final String name, final Role role, final String email) {
        this.id = id;
        this.name = new MemberName(name);
        this.role = role;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}
