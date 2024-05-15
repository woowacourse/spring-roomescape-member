package roomescape.domain;

public class Member {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;

    private Member() {
    }

    public Member(final String name, final String email, final String password) {
        this(name, email, password, Role.USER);
    }

    public Member(final String name, final String email, final String password, final Role role) {
        this(null, name, email, password, role);
    }

    public Member(final Long id, final String name, final String email, final String password) {
        this(id, name, email, password, Role.USER);
    }

    public Member(final Long id, final String name, final String email, final String password, final Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isUser() {
        return role.isUser();
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

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
