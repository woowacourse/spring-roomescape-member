package roomescape.auth.entity;

public class Member {
    private final Long id;
    private final String name;
    private final Role role;
    private final String email;
    private final String password;

    public Member(Long id, String name, String role, String email, String password) {
        this.id = id;
        this.name = name;
        this.role = Role.valueOf(role);
        this.email = email;
        this.password = password;
    }

    public boolean isSamePassword(String password) {
        return this.password.equals(password);
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
