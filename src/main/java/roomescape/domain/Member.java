package roomescape.domain;

public class Member {

    private final Long id;
    private final UserName name;
    private final Email email;
    private final Password password;
    private final Role role;

    public Member(Long id, UserName name, Email email, Password password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(UserName name, Email email, Password password, Role role) {
        this(null, name, email, password, role);
    }

    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
