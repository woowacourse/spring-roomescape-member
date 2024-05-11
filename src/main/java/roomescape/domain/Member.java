package roomescape.domain;

public class Member {

    private final Long id;
    private final String email;
    private final String password;
    private final Name name;
    private final String role;

    public Member(Long id, String email, String password, Name name, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(Long id, String email, String password, String name, String role) {
        this(id, email, password, new Name(name), role);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Name getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
