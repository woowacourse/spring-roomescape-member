package roomescape.member.domain;

public class Member {
    private final Long id;
    private final MemberName memberName;
    private final String email;
    private final String password;
    private final Role role;

    public Member(
            final Long id, final MemberName memberName, final String email,
            final String password, final Role role
    ) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member of(
            final long id, final String name, final String email,
            final String role, final String password
    ) {
        return new Member(id, new MemberName(name), email, password, Role.from(role));
    }

    public static Member of(final String name, final String email, final String password, final String role) {
        return new Member(null, new MemberName(name), email, password, Role.from(role));
    }

    public long getId() {
        return id;
    }

    public MemberName getMemberName() {
        return memberName;
    }

    public String getNameValue() {
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
