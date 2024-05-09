package roomescape.domain.user;

public class User {
    private final UserName userName;
    private final String email;
    private final int password;

    public User(UserName userName, String email, int password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UserName getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public int getPassword() {
        return password;
    }
}
