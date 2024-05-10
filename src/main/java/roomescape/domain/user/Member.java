package roomescape.domain.user;

public class Member {
    private final Long id;
    private final UserName userName;
    private final String email;
    private final int password;

    public Member(UserName userName, String email, int password) {
        this.id = null;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Member(long id, UserName userName, String email, int password) {
        this.id = id;
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

    public long getId() {
        return id;
    }
}
