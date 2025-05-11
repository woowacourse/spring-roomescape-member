package roomescape.model;

public class User {
    private final Long id;
    private final UserName name;
    private final String email;
    private final String password;
    private final Role role;

    public User(Long id, UserName name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNameValue() {
        return name.getName();
    }

    public Role getRole() {
        return role;
    }
}
