package roomescape.domain;

public class LoginMember {
    private final long id;
    private final String name;
    private final String email;
    private final String password;

    public LoginMember(long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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
}
