package roomescape.domain.member;

public class Member {

    private final Long id;
    private final MemberEmail email;
    private final MemberPassword password;
    private final MemberName name;
    private final Role role;

    public Member(Long id, MemberEmail email, MemberPassword password, MemberName name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(Long id, String email, String password, String name, String role) {
        this(
                id,
                new MemberEmail(email),
                new MemberPassword(password),
                new MemberName(name),
                Role.getRole(role)
        );
    }

    public Member(Long id, String email, String password, String name) {
        this(
                id,
                new MemberEmail(email),
                new MemberPassword(password),
                new MemberName(name),
                Role.MEMBER
        );
    }

    public boolean isMismatchedPassword(MemberPassword other) {
        return this.password.isMismatchedPassword(other);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public Role getRole() {
        return role;
    }
}
