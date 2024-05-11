package roomescape.domain.member;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    public Member() {
    }

    public Member(final Long id, final String name, final String email, final String password, final String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
