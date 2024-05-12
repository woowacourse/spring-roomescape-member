package roomescape.domain.member;

public class Member {

    private final Long id;
    private final MemberName name;
    private final MemberEmail email;
    private final MemberPassword password;
    private final MemberRole memberRole;

    public Member(Long id, MemberName name, MemberEmail email, MemberPassword password, MemberRole memberRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
    }

    public boolean isNotAdmin() {
        return !memberRole.isAdmin();
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

    public MemberRole getMemberRole() {
        return memberRole;
    }
}
