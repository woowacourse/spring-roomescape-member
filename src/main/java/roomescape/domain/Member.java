package roomescape.domain;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole memberRole;

    public Member(final Long id, final String name, final String email, final String password,
                  final MemberRole memberRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
    }

    public Member(final String name, final String email, final String password, final MemberRole memberRole) {
        this(null, name, email, password, memberRole);
    }

    public Member(final Long id, final String name, final String email, final MemberRole memberRole) {
        this(id, name, email, null, memberRole);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }
}
