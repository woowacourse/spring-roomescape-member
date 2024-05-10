package roomescape.domain;

public class Member {

    private final Long id;
    private final Name name;
    private final String email;
    private final String password;
    private final MemberRole role;

    public Member(Name name, String email, String password, MemberRole role) {
        this(null, name, email, password, role);
    }

    public Member(Long id, Name name, String email, String password, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isValidPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
