package roomescape.member.business.model.entity;

public class Member {

    private Long id;
    private MemberName name;
    private Email email;
    private Password password;
    private Role role;

    public Member(final Long id, final MemberName name, final Email email, final Password password, final Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
