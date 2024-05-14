package roomescape.domain;

public class Member {

    private final long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(final long id, final String name, final String email, final String password, final Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public long getId() {
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

    public String getRole() {
        return role.name();
    }
}
