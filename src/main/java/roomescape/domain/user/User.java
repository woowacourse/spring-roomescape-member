package roomescape.domain.user;

public class User {
    private final Long id;
    private final UserName name;
    private final UserEmail email;
    private final UserPassword password;

    public User(final Long id, final UserName name, final UserEmail email, final UserPassword password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }


}
