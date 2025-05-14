package roomescape.domain;

public class Member {

    private final long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRoleType role;

    public Member(final long id,
                  final String name,
                  final String email,
                  final String password,
                  final MemberRoleType role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(final String name,
                  final String email,
                  final String password,
                  final MemberRoleType role) {
        this(0, name, email, password, role);
    }

    public long getId() {
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

    public MemberRoleType getRole() {
        return role;
    }
}
