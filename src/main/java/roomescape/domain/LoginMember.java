package roomescape.domain;

public class LoginMember {
    private final String name;
    private final String email;
    private final String password;

    public LoginMember(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
