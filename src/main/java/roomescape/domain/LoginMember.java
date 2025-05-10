package roomescape.domain;

public class LoginMember {

    private final Long id;
    private final String name;
    private final String email;
    private final String role;

    public LoginMember(final Long id, final String name, final String email, final String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
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

    public String getRole() {
        return role;
    }
}
