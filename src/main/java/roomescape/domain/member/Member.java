package roomescape.domain.member;

public class Member {
    private Long id;
    private MemberName name;
    private String email;
    private String password;
    private Role role;

    public Member(
            final Long id, final MemberName name, final String email,
            final String password, final Role role
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public MemberName getName() {
        return name;
    }

    public String getNameValue() {
        return name.getValue();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
