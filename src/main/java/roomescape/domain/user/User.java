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

    public static User of(final String name, final String email, final String password) {
        return new User(new UserName(name), email, password);
    }

    public String getNameValue() {
        return name.getValue();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
