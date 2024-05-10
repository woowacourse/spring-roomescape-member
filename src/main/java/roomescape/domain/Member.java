package roomescape.domain;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final String name;
    private final Role role;

    public Member(Long id, String email, String password, String name, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = Role.valueOf(role);
    }

    public static Member memberRole(Long id, String email, String password, String name) {
        return new Member(id, email, password, name, Role.MEMBER.name());
    }

    public boolean isMismatchedPassword(String password) {
        return !this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
