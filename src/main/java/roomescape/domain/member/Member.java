package roomescape.domain.member;

public class Member {
    private final Long id;
    private final MemberName memberName;
    private final Email email;
    private final Password password;
    private final Role role;

    public Member(Long id, MemberName memberName, String email, String password, Role role) {
        this.id = id;
        this.memberName = memberName;
        this.email = new Email(email);
        this.password = new Password(password);
        this.role = role;
    }

    public Member(Long id, String name, String email, String password, Role role) {
        this(id, new MemberName(name), email, password, role);
    }

    public boolean matchPassword(String rawPassword) {
        return password.match(rawPassword);
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return memberName.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public Role getRole() {
        return role;
    }
}
