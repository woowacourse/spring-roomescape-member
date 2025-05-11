package roomescape.model;

public class User {
    Long id;
    UserName name;
    String email;
    String password;

    public User(Long id, UserName name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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
}
