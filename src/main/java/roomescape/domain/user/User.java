package roomescape.domain.user;

public class User {
    private final Long id;
    private final UserName userName;
    private final String email;
    private final String password;
    private final Role role;

    public User(Long id, UserName userName, String email, String password, Role role) {
        // todo email, password validation
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String name, String email, String password, Role role) {
        this(id, new UserName(name), email, password, role);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
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

    public Role getRole() {
        return role;
    }
}
