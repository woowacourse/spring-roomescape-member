package roomescape.model.user;

public class User {
    private final Long id;
    private final UserName name;
    private final Email email;
    private final Password password;

    public User(Long id, UserName name, Email email, Password password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

}
