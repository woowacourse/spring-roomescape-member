package roomescape.domain;

public class Member {

    private final String name;
    private final String email;
    private final String password;

    public Member(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
}
