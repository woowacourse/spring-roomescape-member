package roomescape.domain;

public final class Member {

    private Long id;
    private String email;
    private String password;
    private String name;
    private Role role;

    public Member() {
    }

    public Member(Long id, String email, String password, String name, String role) {
        this(id, email, password, name, Role.from(role));
    }

    public Member(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
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

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
}
