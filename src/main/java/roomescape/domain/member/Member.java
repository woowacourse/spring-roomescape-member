package roomescape.domain.member;

public class Member {
    private static final int MAX_EMAIL_LENGTH = 30;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private final Long id;
    private final MemberName memberName;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, MemberName memberName, String email, String password, Role role) {
        validateEmail(email);
        validatePassword(password);
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
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

    private void validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("비밀번호는 %d자 이상, %d자 이하여야 합니다.", MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH)
            );
        }
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
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
        return password;
    }

    public Role getRole() {
        return role;
    }
}
