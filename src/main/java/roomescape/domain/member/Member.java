package roomescape.domain.member;

public class Member {
    private static final int MAX_EMAIL_LENGTH = 30;

    private final Long id;
    private final MemberName memberName;
    private final String email;
    private final Password password;
    private final Role role;

    public Member(Long id, MemberName memberName, String email, String password, Role role) {
        validateEmail(email);
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = new Password(password);
        this.role = role;
    }

    public Member(Long id, String name, String email, String password, Role role) {
        this(id, new MemberName(name), email, password, role);
    }

    private void validateEmail(String email) {
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new IllegalArgumentException(String.format("이메일은 %d자 이하여야 합니다.", MAX_EMAIL_LENGTH));
        }
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
        return email;
    }

    public String getPassword() {
        return password.getValue();
    }

    public Role getRole() {
        return role;
    }
}
