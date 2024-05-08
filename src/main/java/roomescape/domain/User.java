package roomescape.domain;

import java.util.Objects;

public class User {
    private final UserName name;
    private final String email;
    private final String password;

    public User(String name, String email, String password) {
        this.name = new UserName(name);
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
