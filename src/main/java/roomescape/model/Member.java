package roomescape.model;

public class Member {
    private final Long id;
    private final Role role;
    private final MemberName memberName;
    private final String email;
    private final String password;

    public Member(final Long id, final Role role, final MemberName memberName, final String email,
                  final String password) {
        this.role = role;
        this.email = email;
        validateNotNull(memberName, password);
        this.id = id;
        this.memberName = memberName;
        this.password = password;
    }

    private void validateNotNull(final MemberName name, final String password) {
        if (name == null || password == null) {
            throw new IllegalArgumentException("유저 생성 시 이름, 비밀번호는 필수입니다.");
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
