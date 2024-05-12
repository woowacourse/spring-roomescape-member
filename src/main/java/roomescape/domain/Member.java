package roomescape.domain;

public class Member {

    private final Long id;
    private final MemberName name;
    private final Email email;
    private final Password password;
    private final MemberRole role;

    public Member(MemberName name, Email email, Password password, MemberRole role) {
        this(null, name, email, password, role);
    }

    public Member(Long id, MemberName name, Email email, Password password, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isValidPassword(Password password) {
        return this.password.equals(password);
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    public Long getId() {
        return id;
    }

    public MemberName getName() {
        return name;
    }
}
