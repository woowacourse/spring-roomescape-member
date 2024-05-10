package roomescape.domain;

public class Member {

    private final Long id;
    private final MemberName name;
    private final MemberEmail email;
    private final MemberPassword password;

    public Member(MemberName name, MemberEmail email, MemberPassword password) {
        this(null, name, email, password);
    }

    public Member(Long id, MemberName name, MemberEmail email, MemberPassword password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(MemberName name) {
        if (name == null) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }
    }

    private void validateEmail(MemberEmail email) {
        if (email == null) {
            throw new IllegalArgumentException("사용자 이메일은 필수입니다.");
        }
    }

    private void validatePassword(MemberPassword password) {
        if (password == null) {
            throw new IllegalArgumentException("사용자 비밀 번호는 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public MemberName getName() {
        return name;
    }

    public MemberEmail getEmail() {
        return email;
    }

    public MemberPassword getPassword() {
        return password;
    }
}
