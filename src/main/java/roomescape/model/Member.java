package roomescape.model;

public class Member {
    private final Long id;
    private final Role role;
    private final MemberName memberName;
    private final String email;
    private final String password;

    public Member(final Long id, final Role role, final MemberName memberName, final String email,
                  final String password) {
        validateNotNull(role, memberName, email, password);
        this.id = id;
        this.role = role;
        this.email = email;
        this.memberName = memberName;
        this.password = password;
    }

    private void validateNotNull(final Role role, final MemberName name, final String email, final String password) {
        if (name == null || password == null || role == null || email == null) {
            throw new IllegalArgumentException("유저 생성 시 역할, 이름, 이메일, 비밀번호,는 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public MemberName getMemberName() {
        return memberName;
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
