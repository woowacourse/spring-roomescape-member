package roomescape.model;

public class Member {

    private final Long id;
    private final MemberName name;
    private final Role role;
    private final String email;
    private final String password;

    public Member(final Long id, final String name, final Role role, final String email) {
        this(id, name, role, email, null);
    }

    public Member(final String name, final Role role, final String email, final String password) {
        this(null, name, role, email, password);
    }

    public Member(final Long id, final String name, final Role role, final String email, final String password) {
        this.id = id;
        this.name = new MemberName(name);
        this.role = role;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
