package roomescape.member.domain;

import roomescape.common.Role;

public class Member {
    private Long id;
    private final MemberName memberName;
    private final String email;
    private final String password;
    private final Role role;

    public Member(MemberName memberName, String email, String password) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.role = Role.MEMBER;
    }

    public Member(Long id, MemberName memberName, String email, String password) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.role = Role.MEMBER;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return memberName.getName();
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
