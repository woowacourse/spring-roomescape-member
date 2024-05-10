package roomescape.domain.member;

public class Member {
    private final Long id;
    private final MemberName memberName;
    private final String email;
    private final String password;

    public Member(MemberName memberName, String email, String password) {
        this.id = null;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
    }

    public Member(long id, MemberName memberName, String email, String password) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
    }

    public MemberName getUserName() {
        return memberName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }
}
