package roomescape.domain.member;

public class Member {

    private final Long id;
    private final MemberName name;
    private final MemberEmail email;
    private final String password;
    private final Role role;

    public Member(Long id, MemberName name, MemberEmail email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
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

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
