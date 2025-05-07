package roomescape.model;

public class User {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean hasSamePassword(final String comparedPassword) {
        return this.password.equals(comparedPassword);
    }
}
