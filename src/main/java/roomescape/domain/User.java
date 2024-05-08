package roomescape.domain;

public class User {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public User(String name, String email, String password) {
        this(null, name, email, password);
    }

    public User(long id, User user) {
        this(id, user.name, user.email, user.password);
    }

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
