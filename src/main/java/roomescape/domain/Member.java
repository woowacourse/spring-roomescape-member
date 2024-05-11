package roomescape.domain;

public class Member {

    private final Long id;
    private final Name name;
    private final String role;
    private final String email;
    private final String password;

    public Member(Long id, Name name, String role, String email, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String name, String role, String email, String password) {
        this(id, new Name(name), role, email, password);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
