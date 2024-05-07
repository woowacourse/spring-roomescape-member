package roomescape.domain.member;

public class Member {

    private final Long id;
    private final MemberName name;
    private final MemberEmail email;
    private final MemberPassword password;

    public Member(Long id, MemberName name, MemberEmail email, MemberPassword password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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
