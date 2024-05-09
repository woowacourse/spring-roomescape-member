package roomescape.domain;

public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public Member(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public boolean isEmailMatches(String email) {
        return this.email.equals(email);
    }

    public boolean isPasswordMatches(String password) {
        return this.password.equals(password);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
