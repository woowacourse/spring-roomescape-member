package roomescape.domain.user;

import java.util.Objects;

public class User {
    private final Long id;
    private final Name name;
    private final Email email;
    private final Password password;
    private final Role role;

    public User(final Long id, final Name name, final Email email, final Password password, final Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User from(final Long id, final String name, final String email, final String password, final String role) {
        return new User(id, new Name(name), new Email(email), new Password(password), Role.from(role));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final User user)) return false;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    public String getName() {
        return name.name();
    }

    public String getRole() {
        return role.getValue();
    }

    public String getEmail() {
        return email.value();
    }

    public String getPassword() {
        return password.value();
    }

    public Long getId() {
        return id;
    }
}
