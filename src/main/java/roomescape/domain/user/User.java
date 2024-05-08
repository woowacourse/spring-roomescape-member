package roomescape.domain.user;

public class User {
    private UserName name;
    private String email;
    private String password;

    public User(final UserName name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserName getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
