package roomescape.domain.member;

public class Member {

    private final MemberName name;
    private final MemberEmail email;
    private final MemberPassword password;

    public Member(MemberName name, MemberEmail email, MemberPassword password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
