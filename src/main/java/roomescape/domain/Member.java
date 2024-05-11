package roomescape.domain;

public class Member {

    private final Long id;
    private final String name;
    private final String role;
    private final String email;
    private final String password;

    public Member(Long id, String name, String role, String email, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public boolean isEmailMatches(String email) {
        return this.email.equals(email);
    }

    public boolean isPasswordMatches(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}
