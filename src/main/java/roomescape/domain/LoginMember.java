package roomescape.domain;

public class LoginMember {
    private final long id;
    private final MemberName name;
    private final String email;
    private final String password;
    private final Role role;

    public LoginMember(long id, MemberName name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public LoginMember(long id, String name, String email, String password, String role) {
        this(id, new MemberName(name), email, password, new Role(role));
    }

    public LoginMember(long id, String name, String email) {
        this(id, new MemberName(name), email, "", new Role(""));
    }

    public String getName() {
        return name.getValue();
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role.getValue();
    }
}
