package roomescape.model.user;

public class User {
    private final Long id;
    private final UserName name;
    private final UserEmail email;
    private final UserPassword password;

    public User(Long id, UserName name, UserEmail email, UserPassword password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

}
