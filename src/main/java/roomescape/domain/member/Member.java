package roomescape.domain.member;

public class Member {

    private final Long id;
    private final String name;
    private final String role;
    private final String email;
    private final String password;

    public Member(String name, String role, String email, String password) {
        this(null, name, role, email, password);
    }

    public Member(Long id, String name, String role, String email, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
