package roomescape.domain;

public class Member {

    private final Long id;
    private final UserName name;
    private final Email email;
    private final Password password;

    public Member(Long id, UserName name, Email email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member(UserName name, Email email, Password password) {
        this(null, name, email, password);
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
}
