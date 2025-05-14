package roomescape.member;

import roomescape.exception.UnAuthorizedException;

public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    public Member(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member of(Long id, String name, String email, String password, String role) {
        return new Member(id, name, email, password, Role.of(role));
    }

    public static Member createWithoutId(String name, String email, String password, String role) {
        return Member.of(null, name, email, password, role);
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

    public Role getRole() {
        return role;
    }

    public void validateMemberRole() {
        if (this.role != Role.ADMIN) {
            throw new UnAuthorizedException("관리자 권한이 필요한 요청입니다.");
        }
    }
}
