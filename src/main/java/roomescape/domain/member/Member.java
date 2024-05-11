package roomescape.domain.member;

public class Member {
    private Long id;
    private Name name;
    private Email email;
    private Password password;
    private Role role;

    public Member() {
    }

    public Member(final Long id, final String name, final String email, final String password, final String role) {
        this.id = id;
        this.name = new Name(name);
        this.email = new Email(email);
        this.password = new Password(password);
        this.role = Role.from(role);
    }

    public Member(final String name, final String email, final String password) {
        this.name = new Name(name);
        this.email = new Email(email);
        this.password = new Password(password);
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }

    public String getRole() {
        return role.name();
    }
}
