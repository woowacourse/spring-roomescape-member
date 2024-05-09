package roomescape.domain;

public class Member {

    private final Name name;
    private final String email;
    private final String password;

    public Member(Name name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
