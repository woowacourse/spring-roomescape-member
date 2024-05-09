package roomescape.domain;

public class Member {

    private final Long id;
    private final Name name;
    private final String email;
    private final String password;

    public Member(Name name, String email, String password) {
        this(null, name, email, password);
    }

    public Member(Long id, Name name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
