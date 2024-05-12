package roomescape.domain.member;

public class Member {

    private final Long id;
    private final MemberName name;
    private final MemberEmail email;
    private final MemberPassword password;
    private final Role role;

    public Member(Long id, MemberName name, MemberEmail email, MemberPassword password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(Long id, Member member) {
        this(id, member.name, member.email, member.password, member.role);
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isNotSame(MemberPassword other) {
        return password.isNotSame(other);
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

    public Role getRole() {
        return role;
    }
}
