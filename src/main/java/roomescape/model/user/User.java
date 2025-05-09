package roomescape.model.user;

public class User {
    private final UserName name;
    private final UserEmail email;
    private final UserPassword password;

    public User(UserName name, UserEmail email, UserPassword password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
