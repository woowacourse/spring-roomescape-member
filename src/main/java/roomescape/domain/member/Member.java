package roomescape.domain.member;

public class Member {
    private final Long id;
    private final MemberName memberName;
    private final Email email;
    private final Password password;
    private final Role role;

    private Member(Long id, MemberName memberName, Email email, Password password, Role role) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(Long id, String name, String email, String password, Role role) {
        this(id, new MemberName(name), new Email(email), new Password(password), role);
    }

    public void validatePassword(String rawPassword, String message) {
        if (!password.match(rawPassword)) {
            throw new IllegalArgumentException(message);
        }
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
