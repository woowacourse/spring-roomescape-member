package roomescape.model;

public class Member {

    private final Long id;
    private final String name;
    private final Role role;
    private final String email;
    private final String password;

    public Member(final Long id, final String name, final Role role, final String email, final String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
