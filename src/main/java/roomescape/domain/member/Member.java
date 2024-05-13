package roomescape.domain.member;

public class Member {
    private Long id;
    private Name name;
    private Email email;
    private Password password;
    private Role role;

    public Member() {
    }

    public Member(Long id, String name, String email, String password, String role) {
        this.id = id;
        this.name = new Name(name);
        this.email = new Email(email);
        this.password = new Password(password);
        this.role = Role.from(role);
    }

    public Member(String name, String email, String password) {
        this.name = new Name(name);
        this.email = new Email(email);
        this.password = new Password(password);
        this.role = Role.MEMBER;
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
