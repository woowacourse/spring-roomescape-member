package roomescape.auth.entity;

public class LoginMember {
    private final Long id;
    private String name;
    private String email;
    private Role role;

    public LoginMember(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = Role.valueOf(role);
    }

    public boolean hasRole(Role role) {
        return this.role == role;
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

    public Role getRole() {
        return role;
    }
}
