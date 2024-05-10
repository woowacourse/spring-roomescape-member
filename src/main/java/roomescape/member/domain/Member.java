package roomescape.member.domain;

public class Member {
    private static final long NO_ID = 0;
    private final long id;
    private final MemberName memberName;
    private final String email;
    private final String password;

    public Member(final long id, final MemberName memberName, final String email, final String password) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
    }

    public Member(String name, String email, String password) {
        this(NO_ID, new MemberName(name), email, password);
    }

    public Member(long id, Member member) {
        this(id, member.memberName, member.email, member.password);
    }

    public Member(long id, String name, String email, String password) {
        this(id, new MemberName(name), email, password);
    }

    public long getId() {
        return id;
    }

    public String getMemberName() {
        return memberName.getValue();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
