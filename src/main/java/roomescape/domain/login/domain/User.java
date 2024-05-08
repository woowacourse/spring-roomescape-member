package roomescape.domain.login.domain;

public class User {
    private Long id;

    private final String email;
    private final String password;
    private final String name;

    public User(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
