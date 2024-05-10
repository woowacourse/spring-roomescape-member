package roomescape.member.domain;

public class Member {
    private static final long NO_ID = 0;
    private final long id;
    private final MemberName memberName;
    private final String email;
    private final String password;
    private final Role role;

    public Member(long id, MemberName memberName, String email, String password, Role role) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(String name, String email, String password, Role role) {
        this(NO_ID, new MemberName(name), email, password, role);
    }

    public Member(long id, Member member) {
        this(id, member.memberName, member.email, member.password, member.role);
    }

    public Member(long id, String name, String email, String password, String role) {
        this(id, new MemberName(name), email, password, Role.valueOf(role));
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

    public String getRole() {
        return role.name();
    }
}
