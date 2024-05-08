package roomescape.domain;

import java.util.Objects;

public class User {
    private final UserName name;
    private final String email;
    private final String password;

    public User(String email, String password) {
        this((UserName) null, email, password);
    }

    public User(String name, String email, String password) {
        this(new UserName(name), email, password);
    }

    private User(UserName name, String email, String password) {
        this.name = name;
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
    }

    public String getName() {
        return name.value();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
