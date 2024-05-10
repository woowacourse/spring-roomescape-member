package roomescape.member.domain;

import roomescape.common.Role;

public class Member {
    private Long id;
    private final MemberName name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(MemberName name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.MEMBER;
    }

    public Member(Long id, MemberName name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.MEMBER;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
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
