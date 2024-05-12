package roomescape.domain;

public class Member {

    private final Long id;
    private final Role role;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, Role role, String name, String email, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(Role role, String email, String password) {
        this(null, role, "유저", email, password);
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
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
}
