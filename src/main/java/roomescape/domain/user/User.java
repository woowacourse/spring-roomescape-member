package roomescape.domain.user;

public class User {
    private final Long id;
    private final UserName userName;
    private final String email;
    private final String password;

    public User(Long id, UserName userName, String email, String password) {
        // todo email, password validation
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(Long id, String name, String email, String password) {
        this(id, new UserName(name), email, password);
    }

    public User(Long id, User user) {
        this(id, user.userName, user.email, user.password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return userName.getValue();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
