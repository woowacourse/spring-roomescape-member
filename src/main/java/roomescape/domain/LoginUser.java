package roomescape.domain;

public class LoginUser {
    private final long id;
    private final String name;
    private final String email;

    public LoginUser(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
